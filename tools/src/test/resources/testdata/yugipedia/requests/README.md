# Requests

## testQueryCategoryMembersByTimestamp

Done on 19/01/2024.

* `ea149b60-110a-38a5-9d35-ba1f79789c49.json` - initial request:
```
GET https://yugipedia.com/api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&generator=categorymembers&gcmsort=timestamp&gcmtitle=Category%3ADuel_Monsters_cards&gcmlimit=10&gcmdir=newer
```

* `801da9c6-a54d-3dde-8c40-644784dc1a8f.json` - next request
```
GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&generator=categorymembers&gcmsort=timestamp&gcmtitle=Category%3ADuel_Monsters_cards&gcmlimit=10&gcmdir=newer&gcmcontinue=2017-11-02%2017%3A48%3A27%7C150083
```

* `63386b25-b81d-36b1-bddc-6f77b87ec389.json` - next request
```
GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&generator=categorymembers&gcmsort=timestamp&gcmtitle=Category%3ADuel_Monsters_cards&gcmlimit=10&gcmdir=newer&gcmcontinue=2017-11-02%2017%3A53%3A08%7C161591
```

## testQueryPagesByTitle

Done on 19/01/2024.

* `94ffcf19-10e8-3079-987e-c96a9f837ec7.json`
```
GET https://yugipedia.com/api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions&rvprop=content|timestamp&titles=LOB%7CETCO
```

## testQueryRecentChanges

Done on 19/01/2024.

* `6d161257-b964-325a-97f3-caa0a7595c1d.json`
```
GET /api.php?action=query&format=json&formatversion=2&redirects=true&prop=revisions|categories&rvprop=content|timestamp&generator=recentchanges&grctype=new|edit|categorize&grctoponly=true&cllimit=max&grclimit=10
```
