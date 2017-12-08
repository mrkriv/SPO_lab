using System;
using System.Collections.Generic;
using System.Linq;

namespace Vername
{
    class Program
    {
        static void Main(string[] args)
        {
            var rand = new Random();
            var input = "I love SPO";

            var key = string.Concat(Enumerable.Range(0, input.Length).Select(x => (char)rand.Next('A', 'Z')));

            var code = EncodeOrDecode(input, key);
            var decode = EncodeOrDecode(code, key);

            Console.WriteLine($"Source: {input}");
            Console.WriteLine($"Key: {key}");
            Console.WriteLine($"Encode: {code}");
            Console.WriteLine($"Decode: {decode}");
        }

        public static string EncodeOrDecode(string input, string key)
        {
            return string.Concat(input.Select((x, i) => (char) (x ^ key[i])));
        }
    }
}