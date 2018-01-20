using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Numerics;
using System.Security.Cryptography;
using System.Text;

namespace SRP
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var server = new Server();
            var client = new Client("vasy", "vidisiki");

            client.Registrate(server);
            client.Auth(server);

            client.Check(server);
        }
    }

    public class Client : UtilitesBase
    {
        public readonly string Login;
        private readonly string password;
        private string salt;
        private BigInteger verfPass;
        private AuthKey authKey;
        private string sessionKey;
        private BigInteger A;

        public Client(string login, string password)
        {
            Login = login;
            this.password = password;
        }

        public void Registrate(Server server)
        {
            salt = GetRandomString(8);
            var x = GetMd5Hash(password + salt);
            var xNum = new BigInteger(StringToByteArray(x));
            
            if (xNum < 0)
                xNum = -xNum;
            
            verfPass = BigInteger.ModPow(g, xNum, n);

            server.Registrate(Login, new ClientData
            {
                Salt = salt,
                VerfPass = verfPass
            });
        }

        public void Auth(Server server)
        {
            var a = new BigInteger(StringToByteArray(GetRandomString(16)));

            if (a < 0)
                a = -a;

            A = BigInteger.ModPow(g, a, n);

            authKey = server.Auth1(Login, a, A);

            if (authKey == null)
                return;
            
            var u = new BigInteger(StringToByteArray(GetMd5Hash(A.ToString() + authKey.B)));

            if (u < 0)
                u = -u;

            if (authKey == null)
                return;

            var p = authKey.B - k * BigInteger.ModPow(g, verfPass, n);
            var s = BigInteger.ModPow(p, a + u * verfPass, n);

            sessionKey = GetMd5Hash(s.ToString());
        }

        public bool Check(Server server)
        {
            if (authKey != null)
            {
                var xor = GetMd5Hash(n.ToString()) + GetMd5Hash(g.ToString());
                var m = GetMd5Hash(xor + salt + A + authKey.B + sessionKey);

                if (server.Check(Login, m))
                {
                    Console.WriteLine("Session key: " + m);
                    return true;
                }
            }

            Console.WriteLine("Failed auth :(");
            return false;
        }
    }

    public class Server : UtilitesBase
    {
        private readonly Dictionary<string, ClientData> database = new Dictionary<string, ClientData>();

        public void Registrate(string login, ClientData data)
        {
            database.Add(login, data);
        }

        public AuthKey Auth1(string login, BigInteger a, BigInteger A)
        {
            if (!database.ContainsKey(login))
                return null;

            var userData = database[login];


            var b = new BigInteger(StringToByteArray(GetRandomString(16)));
            var B = (k * userData.VerfPass + BigInteger.ModPow(g, b, n)) % n;
            userData.A = A;

            var u = new BigInteger(StringToByteArray(GetMd5Hash(A.ToString() + B)));

            if (u < 0)
                u = -u;
            
            var p1 = A * BigInteger.ModPow(userData.VerfPass, u, n);
            var s = BigInteger.ModPow(p1, b, n);

            userData.SessionKey = GetMd5Hash(s.ToString());
            userData.A = A;

            return new AuthKey
            {
                Salt = b,
                B = B
            };
        }

        public bool Check(string login, string clientM)
        {
            if (!database.ContainsKey(login))
                return false;

            var userData = database[login];
            var m = GetMd5Hash(userData.A + clientM + k);
            return string.Compare(m, clientM, StringComparison.Ordinal) != 0;
        }
    }

    public class ClientData
    {
        public string Salt { get; set; }
        public BigInteger VerfPass { get; set; }
        public BigInteger A { get; set; }
        public string SessionKey { get; set; }
    }

    public class AuthKey
    {
        public BigInteger Salt { get; set; }
        public BigInteger B { get; set; }
    }

    public class UtilitesBase
    {
        protected readonly BigInteger g = new BigInteger(2); //генератор по модулю N
        protected readonly BigInteger k = new BigInteger(3); //параметр-множитель

        protected readonly BigInteger n = new BigInteger(StringToByteArray(
            "c037c37588b4329887e61c2da332" +
            "4b1ba4b81a63f9748fed2d8a410c2f" +
            "c21b1232f0d3bfa024276cfd884481" +
            "97aae486a63bfca7b8bf7754dfb327" +
            "c7201f6fd17fd7fd74158bd31ce772" +
            "c9f5f8ab584548a99a759b5a2c0532" +
            "162b7b6218e8f142bce2c30d778468" +
            "9a483e095e701618437913a8c39c3d" +
            "d0d4ca3c500b885fe3")); //безопасное простое

        public static string GetRandomString(int len)
        {
            var rand = new Random();
            return string.Concat(Enumerable.Range(0, len).Select(x => (char) rand.Next('0', '9')));
        }

        public static string GetMd5Hash(string input)
        {
            var md5Hash = MD5.Create();
            var data = md5Hash.ComputeHash(Encoding.UTF8.GetBytes(input));

            return string.Concat(data.Select(x => x.ToString("x2")));
        }

        public static byte[] StringToByteArray(string hex)
        {
            return Enumerable.Range(0, hex.Length)
                .Where(x => x % 2 == 0)
                .Select(x => Convert.ToByte(hex.Substring(x, 2), 16))
                .ToArray();
        }
    }
}