# Lipputoimisto API dokumentaatio

*Versio: 1.0.0*

## URL

`http://.../api`

## Tunnistautuminen ja käyttöoikeudet

Rajapinta käyttää basic authentication.

Käyttöoikeudet ovat rajattu neljän eri roolin mukaisesti:

| Rooli      | Sisäinen nimi | Kuvaus                                                                |
|------------|---------------|-----------------------------------------------------------------------|
| Julkinen   |               | Julkiset toiminnot ovat kaikkien käytettävissä ilman tunnistautumista |
| Myyjä      | MYYJA         | Lipunmyyjät, voivat varata ja lunastaa lippuja                        |
| Järjestäjä | JARJESTAJA    | Tapahtuman järjestäjät, voivat luoda ja hallita omia tapahtumiaan     |
| Admin      | ADMIN         | Kaikki käyttöoikeudet, mukaan lukien käyttäjien hallinta              |

Lähes kaikki rajapinnat palauttavat virhekoodit `401` tai `403` jos tarvittavat käyttöoikeudet puuttuvat,
poikkeuksena GET /kayttajat/{id}

## 1. Tapahtumat

### Hae kaikki tapahtumat

* **Käyttöoikeudet**: Julkinen
* **Metodi**: `GET`
* **Polku**: `/tapahtumat`
* **Vastaus**:
    * `200 OK`
* **Vastauksen runko**:

```json
[
  {
    "tapahtumaId": 1,
    "jarjestajaId": 2,
    "aika": "2026-07-15T18:00:00",
    "paikka": "Olympiastadion",
    "kaupunki": "Helsinki",
    "kuvaus": "Rock Festival 2026",
    "maxLippumaara": 40000,
    "liputJaljella": 200
  },
  {
    "tapahtumaId": 666,
    "jarjestajaId": 666,
    "aika": "6666-06-06T06:06:06",
    "paikka": "Helvetti",
    "kaupunki": "???",
    "kuvaus": "Helvetti jäätyy",
    "maxLippumaara": 666666,
    "liputJaljella": 666666
  }
]
```

### Hae kaikki poistetut tapahtumat

* **Käyttöoikeudet**: Admin
* **Metodi**: `GET`
* **Polku**: `/tapahtumat?poistettu=true`
* **Vastaus**:
    * `200 OK`
* **Vastauksen runko**:

```json
[
  {
    "tapahtumaId": 4,
    "jarjestajaId": 2,
    "aika": "2026-11-20T19:00:00",
    "paikka": "Helsingin jäähalli",
    "kaupunki": "Helsinki",
    "kuvaus": "Winter Rock Night",
    "maxLippumaara": 8500,
    "liputJaljella": 0
  },
  {
    "tapahtumaId": 5,
    "jarjestajaId": 1,
    "aika": "2026-12-05T18:30:00",
    "paikka": "Tampere-talo",
    "kaupunki": "Tampere",
    "kuvaus": "Joulun taikaa",
    "maxLippumaara": 2000,
    "liputJaljella": 1999
  }
]
```

### Hae yksittäinen tapahtuma ID:llä

* **Käyttöoikeudet**: Julkinen
* **Metodi**: `GET`
* **Polku**: `/tapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**:
    * `200 OK`
    * `404 NOT FOUND` (Jos id:tä ei löydy)
* **Vastauksen runko**:

```json
{
  "tapahtumaId": 1,
  "jarjestajaId": 2,
  "aika": "2026-07-15T18:00:00",
  "paikka": "Olympiastadion",
  "kaupunki": "Helsinki",
  "kuvaus": "Rock Festival 2026",
  "maxLippumaara": 40000,
  "liputJaljella": 335
}
```

### Hae omat tapahtumat

* **Käyttöoikeudet**: Järjestäjä
* **Metodi**: `GET`
* **Polku**: `/tapahtumat/omat`
* **Vastaus**:
    * `200 OK`
* **Vastauksen runko**:

```json
[
  {
    "tapahtumaId": 1,
    "jarjestajaId": 2,
    "aika": "2026-07-15T18:00:00",
    "paikka": "Olympiastadion",
    "kaupunki": "Helsinki",
    "kuvaus": "Rock Festival 2026",
    "maxLippumaara": 40000,
    "liputJaljella": 200
  },
  {
    "tapahtumaId": 4,
    "jarjestajaId": 2,
    "aika": "2026-11-20T19:00:00",
    "paikka": "Helsingin jäähalli",
    "kaupunki": "Helsinki",
    "kuvaus": "Winter Rock Night",
    "maxLippumaara": 8500,
    "liputJaljella": 0
  }
]
```

### Luo uusi tapahtuma

* **Käyttöoikeudet**: Järjestäjä
* **Metodi**: `POST`
* **Polku**: `/tapahtumat`
* **Pyynnön runko**:

```json
{
  "aika": "2026-10-05T20:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Stand-up Comedy Night",
  "maxLippumaara": 700
}
```

_huom. `järjestäjäId` tulee suoraan tunnistautuneen käyttäjän ID:stä_

* **Vastaus**:
    * `201 CREATED`
    * `400 BAD REQUEST` (Jos pyynnössä on virheitä)
* **Vastauksen runko**:

```json
{
  "tapahtumaId": 3,
  "jarjestajaId": 1,
  "aika": "2026-10-05T20:00:00",
  "paikka": "Tavastia",
  "kaupunki": "Helsinki",
  "kuvaus": "Stand-up Comedy Night",
  "maxLippumaara": 700,
  "liputJaljella": 700
}
```

### Muokkaa tapahtumaa

* **Käyttöoikeudet**: Järjestäjä (vain omat tapahtumat)
* **Metodi**: `PUT`
* **Polku**: `/tapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Pyynnön runko**:

```json
{
  "aika": "2026-07-15T19:00:00",
  "paikka": "Olympiastadion",
  "kaupunki": "Helsinki",
  "kuvaus": "Rock Festival 2026 (Päivitetty)",
  "maxLippumaara": 45000
}
```

* **Vastaus**:
    * `200 OK`
    * `400 BAD REQUEST` (Jos pyynnössä on virheitä)
    * `404 NOT FOUND` (Jos id:tä ei löydy)
    * `409 CONFLICT` (Jos uusi maxLippumaara < tapahtumaan varatut liput)
* **Vastauksen runko**:

```json
{
  "tapahtumaId": 1,
  "jarjestajaId": 2,
  "aika": "2026-07-15T19:00:00",
  "paikka": "Olympiastadion",
  "kaupunki": "Helsinki",
  "kuvaus": "Rock Festival 2026 (Päivitetty)",
  "maxLippumaara": 45000,
  "liputJaljella": 40000
}
```

### Poista tapahtuma

* **Käyttöoikeudet**: Järjestäjä (vain omat tapahtumat)
* **Metodi**: `DELETE`
* **Polku**: `/tapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**:
    * `204 NO CONTENT`
    * `404 NOT FOUND` (Jos id:tä ei löydy)

---

## 1.1. Lipputyypit

### Hae tapahtuman lipputyypit

* **Käyttöoikeudet**: Julkinen
* **Metodi**: `GET`
* **Polku**: `/tapahtumat/{id}/lipputyypit`
* **Parametrit**: `id (Long)`
* **Vastaus**:
    * `200 OK`
    * `404 NOT FOUND` (Jos tapahtumaa ei löydy)
* **Vastauksen runko**:

```json
[
  {
    "lipputyyppiId": 1,
    "kuvaus": "Aikuinen",
    "lipunHinta": 10.00
  },
  {
    "lipputyyppiId": 2,
    "kuvaus": "Opiskelija",
    "lipunHinta": 5.00
  },
  {
    "lipputyyppiId": 3,
    "kuvaus": "VIP",
    "lipunHinta": 20.00,
    "maxLippumaara": 10,
    "liputJaljella": 3
  }
]
```

### Lisää lipputyyppi tapahtumaan

* **Käyttöoikeudet**: Järjestäjä (vain omat tapahtumat)
* **Metodi**: `POST`
* **Polku**: `/tapahtumat/{id}/lipputyypit`
* **Parametrit**: `id (Long)`
* **Pyynnön runko**:

```json
{
  "kuvaus": "Aikuinen",
  "lipunHinta": 25.00
}
```

* **Vastaus**:
    * `201 CREATED`
    * `400 BAD REQUEST` (Jos pyynnössä on virheitä)
* **Vastauksen runko**:

```json
{
  "lipputyyppiId": 1,
  "kuvaus": "Aikuinen",
  "lipunHinta": 25.00
}
```

#### Lipputyyppikohtainen maxLippumaara

Lipputyypille voidaan määrittää oma maxLippumaara, joka varaa kapasiteettia tapahtuman kokonaiskiintiöstä.

- Jos maxLippumaara on määritelty: Lipputyyppi kuluttaa omaa kiintiötään. Kentät maxLippumaara ja liputJaljella ovat
  mukana vastauksessa.
- Jos maxLippumaaraa ei ole määritelty: Lipputyyppi jakaa yhteisen kapasiteetin muiden sellaisten lipputyyppien kanssa,
  joilla ei ole omaa rajoitusta.

##### Esimerkki:

* **Pyynnön runko**:

```json
{
  "kuvaus": "VIP",
  "lipunHinta": 50.00,
  "maxLippumaara": 100
}
```

* **Vastauksen runko**:

```json
{
  "lipputyyppiId": 3,
  "kuvaus": "VIP",
  "lipunHinta": 50.00,
  "maxLippumaara": 100,
  "liputJaljella": 100
}
```

### Muokkaa tapahtuman lipputyyppiä

* **Käyttöoikeudet**: Järjestäjä (vain omat tapahtumat)
* **Metodi**: `PUT`
* **Polku**: `/tapahtumat/{id}/lipputyypit/{lipputyyppiId}`
* **Parametrit**:
    * `id (Long)`
    * `lipputyyppiId (Long)`
* **Pyynnön runko**:

```json
{
  "kuvaus": "Aikuinen",
  "lipunHinta": 30.00
}
```

* **Vastaus**:
    * `200 OK`
    * `400 BAD REQUEST` (Jos pyynnössä on virheitä)
    * `404 NOT FOUND` (Jos tapahtumaa tai lipputyyppiä ei löydy)
    * `409 CONFLICT` (Jos uusi maxLippumaara < lipputyypillä varatut liput tai uusi maxLippumaara > tapahtuman
      maxLippumaara)
* **Vastauksen runko**:

```json
{
  "lipputyyppiId": 1,
  "kuvaus": "Aikuinen",
  "lipunHinta": 30.00
}
```

### Poista tapahtuman lipputyyppi

* **Käyttöoikeudet**: Järjestäjä (vain omat tapahtumat)
* **Metodi**: `DELETE`
* **Polku**: `/tapahtumat/{id}/lipputyypit/{lipputyyppiId}`
* **Parametrit**:
    * `id (Long)`
    * `lipputyyppiId (Long)`
* **Vastaus**:
    * `204 NO CONTENT`
    * `404 NOT FOUND` (Jos tapahtumaa tai lipputyyppiä ei löydy)

## 2. Liput

### Hae tapahtuman liput

* **Käyttöoikeudet**: Järjestäjä (vain omat tapahtumat)
* **Metodi**: `GET`
* **Polku**: `/tapahtumat/{id}/liput`
* **Vastaus**:
    * `200 OK`
    * `404 NOT FOUND`
    * `403 FORBIDDEN`
* **Vastauksen runko**:

```json
{
  "liput": [
    {
      "lippuId": 1,
      "tapahtumaId": 1,
      "lipputyyppiId": 1,
      "myyntitapahtumaId": 5,
      "koodi": "ABCDEF-123456",
      "hinta": 10.00,
      "status": "VARATTU"
    },
    {
      "lippuId": 23,
      "tapahtumaId": 2,
      "lipputyyppiId": 2,
      "myyntitapahtumaId": 12,
      "koodi": "GHIJKL-789012",
      "hinta": 5.00,
      "status": "LUNASTETTU"
    },
    {
      "lippuId": 25,
      "tapahtumaId": 5,
      "lipputyyppiId": 3,
      "myyntitapahtumaId": 13,
      "koodi": "MNOPQR-345678",
      "hinta": 20.00,
      "status": "PERUTTU"
    }
  ]
}
```

### Varaa lippuja
Lippujen varaaminen luo myyntitapahtuman, joka sisältää varatut liput. Myyntitapahtuman
omistajaksi asetetaan lippuvarauksen tehnyt myyjä.

* **Käyttöoikeudet**: Myyjä
* **Metodi**: `POST`
* **Polku**: `/liput/varaa`
* **Pyynnön runko**:

```json
{
  "tapahtumaId": 1,
  "liput": [
    {
      "lipputyyppiId": 1,
      "qty": 2
    },
    {
      "lipputyyppiId": 2,
      "qty": 1
    }
  ]
}
```

* **Vastaus**:
    * `201 CREATED`
    * `400 BAD REQUEST` (Jos pyynnössä on virheitä)
    * `404 NOT FOUND` (Jos tapahtumaa tai lipputyyppiä ei löydy)
    * `409 CONFLICT` (Jos lippuja ei ole jäljellä tapahtumaan tai lipputyyppiin)
* **Vastauksen runko**:

```json
{
  "myyntitapahtumaId": 123,
  "summa": 80.00,
  "liput": [
    {
      "lipputyyppiId": 1,
      "koodi": "ABCDEF-123456"
    },
    {
      "lipputyyppiId": 1,
      "koodi": "FEDCBA-654321"
    },
    {
      "lipputyyppiId": 2,
      "koodi": "GHIJKL-789012"
    }
  ]
}
```

### Lunasta lippu

* **Käyttöoikeudet**: Myyjä
* **Metodi**: `POST`
* **Polku**: `/liput/lunasta`
* **Pyynnön runko**:

```json
{
  "koodi": "ABCDEF-123456"
}
```

* **Vastaus**:
    * `200 OK`
    * `404 NOT FOUND` (Jos koodia ei löydy)
    * `409 CONFLICT` (Jos lippu on jo lunastettu tai peruttu)

### Hae lippu koodilla

* **Käyttöoikeudet**: Myyjä
* **Metodi**: `GET`
* **Polku**: `/liput/koodi/{koodi}`
* **Vastaus**:
    * `200 OK` (Lipun tiedot, myös lunastettujen ja peruttujen lippujen tila)
    * `401 UNAUTHORIZED` (Tunnistautuminen puuttuu tai epäonnistuu)
    * `403 FORBIDDEN` (Käyttäjällä ei ole myyjän käyttöoikeutta)
    * `404 NOT FOUND` (Koodia ei löydy)
* **Vastauksen runko**:

```json
{
  "lippuId": 1,
  "tapahtumaId": 1,
  "lipputyyppiId": 1,
  "myyntitapahtumaId": 1,
  "koodi": "ABCDEF-123456",
  "hinta": 15.00,
  "status": "VARATTU"
}
```

### Peruuta varaus

* **Käyttöoikeudet**: Myyjä
* **Metodi**: `POST`
* **Polku**: `/liput/peru`
* **Pyynnön runko**:

```json
{
  "koodi": "ABCDEF-123456"
}
```

* **Vastaus**:
    * `200 OK`
    * `404 NOT FOUND` (Jos koodia ei löydy)
    * `409 CONFLICT` (Jos lippua ei voi perua)

## 3. Myyntitapahtumat

### Hae kaikki myyntitapahtumat

* **Käyttöoikeudet**: Admin
* **Metodi**: `GET`
* **Polku**: `/myyntitapahtumat`
* **Vastaus**:
    * `200 OK`
* **Vastauksen runko**:

```json
[
  {
    "myyntitapahtumaId": 123,
    "myyjaId": 2,
    "maksuaika": "2026-07-15T14:30:00",
    "summa": 33.00
  },
  {
    "myyntitapahtumaId": 124,
    "myyjaId": 3,
    "maksuaika": "2026-07-15T15:10:00",
    "summa": 25.00
  }
]
```

### Hae yksittäinen myyntitapahtuma ID:llä

* **Käyttöoikeudet**: Admin
* **Metodi**: `GET`
* **Polku**: `/myyntitapahtumat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**:
    * `200 OK`
    * `404 NOT FOUND` (Jos id:tä ei löydy)
* **Vastauksen runko**:

```json
{
  "myyntitapahtumaId": 123,
  "myyjaId": 2,
  "maksuaika": "2026-07-15T14:30:00",
  "summa": 33.00,
  "liput": [
    {
      "lippuId": 1,
      "lipputyyppiId": 1,
      "koodi": "ABCDEF-123456",
      "hinta": 10.00,
      "lipunStatus": "VARATTU"
    },
    {
      "lippuId": 2,
      "lipputyyppiId": 2,
      "hinta": 15.00,
      "lipunStatus": "VARATTU",
      "koodi": "PSMWVT-012597"
    },
    {
      "lippuId": 3,
      "lipputyyppiId": 3,
      "hinta": 8.00,
      "lipunStatus": "VARATTU",
      "koodi": "ZKMPDJ-076871"
    }
  ]
}
```

### Hae myyjän omat myyntitapahtumat

* **Käyttöoikeudet**: Myyjä (vain omat myyntitapahtumat)
* **Metodi**: `GET`
* **Polku**: `/myyntitapahtumat/omat`
* **Vastaus**:
    * `200 OK`
* **Vastauksen runko**:

```json
[
  {
    "myyntitapahtumaId": 123,
    "myyjaId": 2,
    "maksuaika": "2026-07-15T14:30:00",
    "summa": 33.00
  },
  {
    "myyntitapahtumaId": 126,
    "myyjaId": 2,
    "maksuaika": "2026-07-16T10:10:00",
    "summa": 58.00
  }
]
```

### Hae tietyn myyjän myyntitapahtumat

* **Käyttöoikeudet**: Admin
* **Metodi**: `GET`
* **Polku**: `/myyntitapahtumat/myyja/{id}`
* **Vastaus**:
    * `200 OK`
    * `404 NOT FOUND` (Jos myyjää ei löydy)
* **Vastauksen runko**:

```json
[
  {
    "myyntitapahtumaId": 123,
    "myyjaId": 2,
    "maksuaika": "2026-07-15T14:30:00",
    "summa": 33.00
  },
  {
    "myyntitapahtumaId": 126,
    "myyjaId": 2,
    "maksuaika": "2026-07-16T10:10:00",
    "summa": 58.00
  }
]
```

## 4. Käyttäjät

### Hae yksittäinen käyttäjä ID:llä

Palauttaa vain Järjestäjä-roolin käyttäjiä jos pyynnön tekijä ei ole tunnistautunut.

* **Käyttöoikeudet**: Julkinen
* **Metodi**: `GET`
* **Polku**: `/kayttajat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**:
  * `200 OK`
  * `404 NOT FOUND` (Jos käyttäjää ei löydy) (Jos löydetyn käyttäjän rooli ei ole järjestäjä ja pyynnön tekijä ei ole tunnistautunut)
* **Vastauksen runko**:

```json
{
  "kayttajaId": 3,
  "nimi": "örinämörkö666",
  "email": "kuoleman_enkeli92@jippii.fi",
  "rooli": "JARJESTAJA"
}
```

### Hae käyttäjät

* **Käyttöoikeudet**: Admin
* **Metodi**: `GET`
* **Polku**: `/kayttajat`
* **Vastaus**:
    * `200 OK`
    * `403 FORBIDDEN`
* **Vastauksen runko**:

```json
[
  {
    "kayttajaId": 1,
    "nimi": "admin",
    "email": "admin@example.com",
    "rooli": "ADMIN"
  },
  {
    "kayttajaId": 2,
    "nimi": "merja1964",
    "email": "merjameikalainen@example.com",
    "rooli": "MYYJA"
  },
  {
    "kayttajaId": 3,
    "nimi": "örinämörkö666",
    "email": "kuoleman_enkeli92@jippii.fi",
    "rooli": "JARJESTAJA"
  }
]
```

### Luo uusi käyttäjä (admin only)

* **Käyttöoikeudet**: Admin
* **Metodi**: `POST`
* **Polku**: `/kayttajat`
* **Pyynnön runko**:

```json
{
  "nimi": "uusi_kayttaja",
  "email": "uusi@example.com",
  "salasana": "esimerkkisalasana",
  "rooli": "MYYJA"
}
```

* **Vastaus**:
    * `201 CREATED`
    * `400 BAD REQUEST` (Jos pyynnössä on virheitä)
    * `403 FORBIDDEN`
* **Vastauksen runko**:

```json
{
  "kayttajaId": 4,
  "nimi": "uusi_kayttaja",
  "email": "uusi@example.com",
  "rooli": "MYYJA"
}
```

* **Käyttöoikeudet**: Admin
### Muokkaa käyttäjää (admin only)

* **Metodi**: `PUT`
* **Polku**: `/kayttajat/{id}`
* **Parametrit**: `id (Long)`
* **Pyynnön runko**:

```json
{
  "nimi": "paivitetty_kayttaja",
  "email": "paivitetty@example.com",
  "salasana": "uusi_esimerkkisalasana",
  "rooli": "JARJESTAJA"
}
```

* **Vastaus**:
    * `200 OK`
    * `400 BAD REQUEST` (Jos pyynnössä on virheitä)
    * `403 FORBIDDEN`
    * `404 NOT FOUND` (Jos käyttäjää ei löydy)
* **Vastauksen runko**:

```json
{
  "kayttajaId": 4,
  "nimi": "paivitetty_kayttaja",
  "email": "paivitetty@example.com",
  "rooli": "JARJESTAJA"
}
```

### Poista käyttäjä (admin only)

* **Käyttöoikeudet**: Admin
* **Metodi**: `DELETE`
* **Polku**: `/kayttajat/{id}`
* **Parametrit**: `id (Long)`
* **Vastaus**:
    * `204 NO CONTENT`
    * `403 FORBIDDEN`
    * `404 NOT FOUND` (Jos käyttäjää ei löydy)
