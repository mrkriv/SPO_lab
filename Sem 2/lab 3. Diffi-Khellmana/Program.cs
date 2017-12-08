using System;
using System.Linq;

namespace Diffi_Khellmana
{
    class Program
    {
        static void Main(string[] args)
        {
            var medved = new Entity("Медведев");
            var naval = new Entity("Навальный");

            medved.Join(naval);
            naval.Join(medved);

            medved.Recive(naval.Send("Отвечай за виноградки!"));
            naval.Recive(medved.Send("Omae wa mou shindeiru!"));
            medved.Recive(naval.Send("NANI ?!"));
        }
    }

    class Entity
    {
        private const int PrivateKeyLen = 32;
        private const int Root = 3;
        private const int PublicConst = 17;

        private readonly string _privateKey;
        private readonly string _name;

        public string PublicMyKey { get; }
        public string PublicOtherKey { get; set; }

        public Entity(string name)
        {
            _name = name;
            var rand = new Random();

            _privateKey = string.Concat(Enumerable.Range(0, PrivateKeyLen).Select(x => (char) rand.Next('A', 'Z')));
            PublicMyKey = string.Concat(_privateKey.Select(x =>
                NormalizeChar(Math.Pow(Root, x) % PublicConst)));

            Console.WriteLine($"{name} have private key {_privateKey}");
            Console.WriteLine($"{name} have public key {PublicMyKey}");
        }

        public void Join(Entity other)
        {
            PublicOtherKey = other.PublicMyKey;
        }

        public string Send(string message)
        {
            var encodeMessage = Magic(message);
            Console.WriteLine($"{_name}: {message} -> {encodeMessage}");

            return encodeMessage;
        }

        public string Recive(string message)
        {
            var decodeMessage = Magic(message);
            Console.WriteLine($"{_name}: {message} -> {decodeMessage}");

            return decodeMessage;
        }

        private string Magic(string message)
        {
            var key = string.Concat(PublicOtherKey.Select((x, i) => Math.Pow(Root, _privateKey[i] * x) % PublicConst));
            return string.Concat(message.Select((x, i) => (char) (x ^ key[i % key.Length])));
        }

        private static char NormalizeChar(double input)
        {
            return (char) ('A' + input % ('Z' - 'A'));
        }
    }
}