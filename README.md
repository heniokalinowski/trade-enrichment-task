- How to run the service.
Run TradeEnrichmentApplication::main

- How to use the API.
curl http://localhost:8080/api/v1/enrich --form "file=@src/test/resources/trade.csv"

- Any limitations of the code.
Tested with only a provided data.
Keeps data in HashMap, therefore entire mapping has to fit in RAM.

- Any discussion/comment on the design.
This is the simplest implementation, without any optimizations. It has just 4 classes.

- Any ideas for improvement if there were more time available.
1. load balancer + distributed service,
2. streaming via Apache Beam,
3. streaming via WebSocket,
4. Kotlin + coroutines,
5. reactive streams,
6. in distributed system, some servers can keep different parts of mappings in memory, so we can enrich different parts at once