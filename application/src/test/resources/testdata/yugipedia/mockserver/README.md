# Yugipedia Mock Server

This directory contains mocked-requests downloaded from the real API (https://yugipedia.com).
The mockserver can be used to:

* Ensure that requests params

The `mocks.json` contains the information to map the requests to the actual file containing the information,
which is in the form:

```json
[
    {
      "folder": "queryCategoryMembersByTimestamp",
      "file": "ea149b60-110a-38a5-9d35-ba1f79789c49.json",
      "request": "/api.php?action=query&format=json&formatversion=2",
      "date": "2024-04-16"
    }
]
```

Responses are located into `${folder}/${file}` and are associated with a real request (`${request}`) performed on the date `${date}`.

IMPORTANT: the request is not URL-encoded.

##
