## Goal
The goal of the project is to implement a parser that takes two JSONObjects (net.sf.json.JSONObject, or another similar framework) as input and returns a JSONObject that details the changes between the two input objects.

The focus should be on readability and maintainability of the code, not on performance. The idea is to give us an indication of your coding style.

## Setup
- Install Maven
- Run "mvn install" to download dependencies
- Implement functionality described in "Assignments" below

Feel free to use any external libraries you want, just add them to the .pom file.

## Assignments
- Implement the parser
- The parser should take two JSONObjects as input, and output a new JSONObject on the following format:

```
{
  "meta": [
    // List of the fields updated with before and after values
    // For time fields the timezone should be CEST (Oslo - UTC+2)
    {
      "field": "title",
      "before": "before",
      "after": "after"
    }
  ],
  // Object describing the different updates to the candidates list
  "candidates": {
    // List of candidates that have been edited (using the "id" field as identifier)
    "edited": [
        {
            "id": 0
        }
    ],
    // List of candidates that have been added (using the "id" field as identifier)
    "added": [
        {
            "id": 0
        }
   	],
   	// List of candidates that have been removed (using the "id" field as identifier)
    "removed": [
        {
            "id": 0
        }
    ]
  }
}
```
See test/resources/diff.json for how the report should look. This report is based on the diff between before.json and after.json. The "id" field should be used as an identifier. Make assumptions as needed.

- Implement unit tests
- Document the code, including any assumptions and simplifications you make


## DEVELOPER NOTES
1. net.sf.json-lib.json-lib has vulnerabilities (according to the Maven repository) so I decided to use org.json.json instead.

2. The requirement says that it has to be in CEST (Oslo - UTC+2), however at the time of this test, Oslo is UTC+1, so for the sake of this test I'm deciding to keep if fixed to UTC+2 (regardless of daylight savings).