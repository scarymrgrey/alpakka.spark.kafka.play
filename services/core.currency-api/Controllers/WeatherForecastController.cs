using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace core.currency_api.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class CurrencyController : ControllerBase
    {
        private Dictionary<String, double> rates = new Dictionary<String, double>() {
                {"USD_PLN" , 4.1 },
                {"PLN_USD" , 1 / 4.1},
                {"USD_EUR" , 1.1},
                {"EUR_USD" , 1 / 1.1}
                };

        private readonly ILogger<CurrencyController> _logger;

        public CurrencyController(ILogger<CurrencyController> logger)
        {
            _logger = logger;
        }

        [HttpGet]
        public IActionResult Get()
        {
            return Ok("working!!!");
        }

        [HttpPost]
        public IActionResult Get(List<CurrencyRequest> requests)
        {
            foreach (var req in requests)
            {
                return Ok(new CurrencyResponse
                {
                    Id = req.Id,
                    initial = req.value,
                    converted = req.value * 4.1,
                    from_currency = req.from_currency,
                    to_currency = req.to_currency
                });
            };

            return new BadRequestResult();
        }
    }
}
