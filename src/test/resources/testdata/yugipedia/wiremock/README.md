# Yugipedia Wiremock Server files

This directory contains wiremock files to mock requests downloaded from the real API (https://yugipedia.com).
The files are grouped by method on the API, both in the `__files` and the `mappings` folder.

For the mappings, the following is required:

- Include a metadata tag with the `actual_request`, so it can be reproduced by copy-past on a browser or tool (such Postman)
- Include the `absent_fields` tag to explain why each field marked with `"absent": true` is required
- Include the following headers:
  - `Content-Type` as it might be required by the API implementation
  - `Date` to document when the requested files were downloaded (so we can update if our expectations change)
