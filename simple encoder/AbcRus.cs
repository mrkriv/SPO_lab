namespace encode_test
{
    public static class AbcRus
    {
        public static int InvalidIndex = -1;
        public static char Min = 'а';
        public static char Max = 'я';
        public static int Size = Max - Min + 1;

        public static int ToIndex(char Symbol)
        {
            Symbol = char.ToLower(Symbol);

            if (IsValid(Symbol))
            {
                return Symbol - Min;
            }

            return InvalidIndex;
        }

        public static bool IsValid(char Symbol)
        {
            return Symbol >= Min && Symbol <= Max;
        }

        public static char FromIndex(int Index)
        {
            return (char)(Min + Index);
        }
    }
}