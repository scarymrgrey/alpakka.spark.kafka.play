using System;

namespace core.currency_api
{
    public class CurrencyRequest
    {
        public String Id { get; set; }

        public double value { get; set; }
        
        public string from_currency { get; set; }

        public string to_currency { get; set; }
    }

    
    public class CurrencyResponse
    {
        public String Id { get; set; }

        public double initial { get; set; }

        public double converted { get; set; }

        public string from_currency { get; set; }

        public string to_currency { get; set; }
    }
}
