using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

namespace simple_encoder
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            Process(File.ReadAllText("input.txt"), "интеграл");
        }

        private static void Process(string input, string key)
        {
            // for real compare
            //input = string.Concat(input.Where(AbcRus.IsValid));

            Console.WriteLine($"Input:\n{input}\n");
            Console.WriteLine($"Key: {key}\n\n");

            var code = Encode(input, key);

            Console.WriteLine($"Encode:\n{code}\n");
            //Console.WriteLine($"Real decode:\n{Decode(code, key)}\n");

            var decode = FrequencyDecode(code, key);
            var rate = CompareString(input, decode);

            Console.WriteLine($"Frequency decode:\n{decode}\n");
            Console.WriteLine($"Coincidence rate: {rate:P}%");
        }

        public static string Encode(string input, string key)
        {
            return ReplaceString(input, GetReplaceMap(key));
        }

        public static string Decode(string input, string key)
        {
            return ReplaceString(input, ReverceReplaceMap(GetReplaceMap(key)));
        }

        public static string FrequencyDecode(string input, string key)
        {
            var fileText = File.ReadAllText("VoinaIMir.txt");

            var table_target = BuildFrequencyTable(fileText);
            var table_source = BuildFrequencyTable(input);

            var replace_map = new char[AbcRus.Size];

            for (var i = 0; i < replace_map.Length; i++)
            {
                var index = table_target.FindIndex(x => x.Item1 == i);

                replace_map[i] = AbcRus.FromIndex(table_source[index].Item1);
            }

            return ReplaceString(input, replace_map);
        }

        private static string ReplaceString(string input, IReadOnlyList<char> replace_map)
        {
            var result = "";

            foreach (var c in input)
            {
                if (AbcRus.IsValid(c))
                {
                    result = result + replace_map[AbcRus.ToIndex(c)];
                }
                else
                {
                    result = result + c;
                }
            }

            return result;
        }

        private static char[] GetReplaceMap(string key)
        {
            return key.AsEnumerable().Concat(
                    Enumerable.Range(AbcRus.Min, AbcRus.Size).Select(c => (char)c)
                        .Where(c => !key.Contains(c)))
                .ToArray();
        }

        private static char[] ReverceReplaceMap(IReadOnlyList<char> replace_map)
        {
            var reverce = new char[replace_map.Count];

            for (var i = 0; i < replace_map.Count; i++)
            {
                var c = replace_map[i];
                reverce[AbcRus.ToIndex(c)] = AbcRus.FromIndex(i);
            }

            return reverce.ToArray();
        }

        private static List<Tuple<int, long>> BuildFrequencyTable(string Text)
        {
            var table = new Dictionary<int, long>();

            for (var i = 0; i < AbcRus.Size; i++)
            {
                table.Add(i, 0);
            }

            foreach (var c in Text)
            {
                if (AbcRus.IsValid(c))
                    table[AbcRus.ToIndex(c)]++;
            }

            return table.Select(x => Tuple.Create(x.Key, x.Value)).OrderBy(x => x.Item2).ToList();
        }

        private static double CompareString(string a, string b)
        {
            if (a.Length != b.Length)
                return 0;

            double count = a.Where((t, i) => t == b[i]).LongCount();
            return count / a.Length;
        }
    }
}