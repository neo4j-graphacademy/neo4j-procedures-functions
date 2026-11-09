// Fictitious stocks and headlines for the GraphAcademy course.
// Safe to run more than once: it clears the Stock and Headline nodes first.
MATCH (n)
WHERE n:Stock OR n:Headline
DETACH DELETE n;

UNWIND [
  {symbol: 'ACME', headline: 'Acme posts record profits and raises its outlook'},
  {symbol: 'ACME', headline: 'Acme shares fall after a terrible quarter'},
  {symbol: 'ACME', headline: 'Acme announces a new warehouse opening in May'},
  {symbol: 'ACME', headline: 'Analysts love the great margins at Acme'},
  {symbol: 'ACME', headline: 'Acme recalls a faulty product, an awful setback'},
  {symbol: 'BLUX', headline: 'Blux wins a major contract and shares jump'},
  {symbol: 'BLUX', headline: 'Blux faces a damaging lawsuit over its patents'},
  {symbol: 'BLUX', headline: 'Blux publishes its annual report on Friday'},
  {symbol: 'BLUX', headline: 'Investors are thrilled by the excellent Blux launch'},
  {symbol: 'BLUX', headline: 'Blux chief executive resigns in a shocking move'},
  {symbol: 'CORV', headline: 'Corv beats forecasts with strong sales growth'},
  {symbol: 'CORV', headline: 'Corv cuts 500 jobs as demand collapses'},
  {symbol: 'CORV', headline: 'Corv schedules its shareholder meeting for June'},
  {symbol: 'CORV', headline: 'Corv wins praise for its outstanding customer service'},
  {symbol: 'CORV', headline: 'Corv warns of a horrible year ahead'},
  {symbol: 'DYNA', headline: 'Dyna expands into three new markets'},
  {symbol: 'DYNA', headline: 'Dyna stock soars on wonderful earnings'},
  {symbol: 'DYNA', headline: 'Dyna suffers a terrible data breach'},
  {symbol: 'DYNA', headline: 'Dyna appoints a new head of finance'},
  {symbol: 'DYNA', headline: 'Dyna is fined for poor safety standards'}
] AS row
MERGE (s:Stock {symbol: row.symbol})
CREATE (h:Headline {id: randomUUID(), symbol: row.symbol, headline: row.headline})
CREATE (s)-[:HAS_HEADLINE]->(h);
