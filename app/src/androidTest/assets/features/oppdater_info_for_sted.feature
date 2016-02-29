#language: no

  Egenskap: Oppdater informasjon for sted

    Bakgrunn: Som bruker
    Vil jeg manuelt oppdatere informasjonen for et sted
    Slik at jeg er sikker på at jeg alltid har det siste aktuelle varslet som er tilgjengelig

    Scenario: Det finnes nye oppdateringer
    Gitt at appen er åpnet
    Og brukeren er inne på hovedsiden
    Når brukeren trykker på oppdater-knappen
    Så skal ny værinformasjonen framkomme

    Scenario: Det finnes ikke nye oppdateringer
    Gitt at appen er åpnet
    Og brukeren er inne på hovedsiden
    Når brukeren trykker på oppdater-knappen
    Så forekommer ingen endringer hvis nye oppdateringer ikke er tilgjengelig