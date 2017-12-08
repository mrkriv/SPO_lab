using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;

namespace lab2
{
    public static class Program
    {
        public static void Main(string[] args)
        {
            var rand = new Random();
            
            var input = File.ReadAllText("Input.txt");
            var key = new string(input.Select(x => (char) rand.Next(char.MinValue, char.MaxValue)).ToArray());
            var code = Encode(input, key);
            var decode = Encode(code, key);
            
            Console.WriteLine($"Text:\n{input}\n");
            Console.WriteLine($"Key:\n{key}\n\n");

            Console.WriteLine($"Coded text:\n{code}\n");
            Console.WriteLine($"Decoded text:\n{decode}\n");
        }

        public static string Encode(string text, string key)
        {
            if(text.Length != key.Length)
                throw new Exception($"text.Length != key.Length ({text.Length} != {key.Length})");
                
            var str = "";

            for (var i = 0; i < text.Length; i++)
            {
                str += text[i] ^ key[i];
            }
            
            return str;
        }
    }
}