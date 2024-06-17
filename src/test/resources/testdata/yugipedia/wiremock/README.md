# Yugipedia Wiremock Server files

This directory contains wiremock files to mock requests downloaded from the real API (https://yugipedia.com).
The files are grouped by method on the API, both in the `__files` and the `mappings` folder.

For the mappings, the following is required:

- Include the `metadata` field witht the following tags:
  - `actual_request` to define the `/api.php` query, so it can be reproduced by copy-past on a browser or tool (such Postman)
  - `absent_fields` with an object mapping the `fields` marked with `"absent": true` and the explanation of why it is required for the mock
- Include the following headers:
  - `Content-Type` as it might be required by the API implementation
  - `Date` to document when the requested files were downloaded (so we can update if our expectations change)
