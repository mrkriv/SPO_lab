using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;

namespace lab_4._RSA
{
    class Program
    {
        static void Main(string[] args)
        {
            var medved = new Entity("Медведев");
            var naval = new Entity("Навальный");

            medved.Recive(naval.Send("Отвечай за виноградки!", medved.PublicKey));
            naval.Recive(medved.Send("Omae wa mou shindeiru!", naval.PublicKey));
            medved.Recive(naval.Send("NANI ?!", medved.PublicKey));
        }
    }

    struct PublicKey
    {
        public long E { get; set; }
        public long N { get; set; }
    }

    struct PrivateKey
    {
        public long D { get; set; }
        public long N { get; set; }
    }

    class Entity
    {
        private readonly string _name;

        private PrivateKey PrivateKey { get; set; }
        public PublicKey PublicKey { get; set; }

        public Entity(string name)
        {
            var rand = new Random();
            var keysize = 3;
            var keyPow = (int) Math.Pow(10, keysize - 1);

            long p = 0;
            long q = 0;

            do
            {
                p = rand.Next(keyPow, keyPow * 10 - 1);
            } while (!IsSimple(p));

            do
            {
                q = rand.Next(keyPow, keyPow * 10 - 1);
            } while (!IsSimple(q));
            
            var n = p * q;
            var f = (p - 1) * (q - 1);
            var d = CalculateD(f);
            var e = CalculateE(d, f);

            PublicKey = new PublicKey {E = e, N = n};
            PrivateKey = new PrivateKey {D = d, N = n};

            _name = name;

            Console.WriteLine($"{name} have p={p}, q={q}, n={n}, e={e}, d={d}");
        }

        private static long CalculateD(long m)
        {
            var d = m - 1;

            for (long i = 2; i <= m; i++)
                if ((m % i == 0) && (d % i == 0))
                {
                    d--;
                    i = 1;
                }

            return d;
        }

        private static long CalculateE(long d, long m)
        {
            var e = 10;

            while (true)
            {
                if ((e * d) % m == 1)
                    break;

                e++;
            }

            return e;
        }

        private static bool IsSimple(long n)
        {
            if (n < 2)
                return false;

            if (n == 2)
                return true;

            for (long i = 2; i < n; i++)
                if (n % i == 0)
                    return false;

            return true;
        }

        public List<long> Send(string message, PublicKey publicKey)
        {
            var msg = message.Select(x => (long)BigInteger.ModPow(x, publicKey.E, publicKey.N)).ToList();

            Console.WriteLine($"{_name}: {message} -> {string.Join(", ", msg)}");

            return msg;
        }

        public string Recive(List<long> message)
        {
            var msg = string.Concat(message.Select(x =>
                char.ConvertFromUtf32((int)BigInteger.ModPow(x, PrivateKey.D, PrivateKey.N))));

            Console.WriteLine($"{_name}: {string.Join(", ", message)} -> {msg}");

            return msg;
        }
    }
}