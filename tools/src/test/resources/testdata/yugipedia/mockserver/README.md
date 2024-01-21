# Requests

This directory contains mocked-requests downloaded from the real API.
Each method section contains a list of files with the corresponding date when the request was downloaded.
In addition, the request path (without the host) is provided as a reference on where it is mocked.

## queryCategoryMembersByTimestamp

* File: `ea149b60-110a-38a5-9d35-ba1f79789c49.json` - 19/01/2024
  ```
  GET https://yugipedia.com/api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&generator=categorymembers&gcmsort=timestamp&gcmtitle=Category%3ADuel_Monsters_cards&gcmlimit=10&gcmdir=newer
  ```
* File: `801da9c6-a54d-3dde-8c40-644784dc1a8f.json` - 19/01/2024
  ```
  GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&generator=categorymembers&gcmsort=timestamp&gcmtitle=Category%3ADuel_Monsters_cards&gcmlimit=10&gcmdir=newer&gcmcontinue=2017-11-02%2017%3A48%3A27%7C150083
  ```

* File: `63386b25-b81d-36b1-bddc-6f77b87ec389.json` - 19/01/2024
  ```
  GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&generator=categorymembers&gcmsort=timestamp&gcmtitle=Category%3ADuel_Monsters_cards&gcmlimit=10&gcmdir=newer&gcmcontinue=2017-11-02%2017%3A53%3A08%7C161591
  ```

## queryRecentChanges

* File: `6d161257-b964-325a-97f3-caa0a7595c1d.json` - 20/01/2024
  ```
  GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions|categories&rvprop=content|timestamp&generator=recentchanges&grctype=new|edit|categorize&grctoponly=true&cllimit=max&grclimit=10
  ```

* File: `6e830fd3-2129-3126-86bf-d537dbc0a0f8.json` - 20/01/2024
  ```
  GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions|categories&rvprop=content|timestamp&generator=recentchanges&grctype=new|edit|categorize&grctoponly=true&cllimit=max&grclimit=10&grccontinue=20240120070017|4675744
  ```

* File: `b5afe4f0-e943-3f2a-8ff0-20bb4cebf1ca.json` - 20/01/2024
  ```
  GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions|categories&rvprop=content|timestamp&generator=recentchanges&grctype=new|edit|categorize&grctoponly=true&cllimit=max&grclimit=10&grccontinue=20240120022338%7C4675720
  ```

* File: `67a629a6-9382-3b72-911c-cff00dfa55b5.json` - 21/01/2024
  ```
  GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions|categories&rvprop=content|timestamp&generator=recentchanges&grctype=new|edit|categorize&grctoponly=true&cllimit=max&grclimit=10&grcstart=2024-01-01T00%3A00%3A00Z
  ```

## queryPagesByTitle

* File: `cf797f99-25fd-3695-87c4-78e14cf0fe66.json` - batchcomplete (no titles), should be always the same
  ```
  GET /api.php?action=query&format=json&formatversion=2&redirects=true&generator=pages&prop=revisions&rvprop=content|timestamp
  ```

* File: `94ffcf19-10e8-3079-987e-c96a9f837ec7.json` - 20/01/2024
  ```
  GET https://yugipedia.com/api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&titles=LOB%7CETCO
  ```
