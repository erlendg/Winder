# language: no

Egenskap: Legg til nytt sted

  Bakgrunn: Som en bruker
            Vil jeg legge til et nytt sted
            Slik at jeg mottar varsel for dette stedet

    Scenario: Bruker har maks antall steder registrert
      Gitt at bruker har ti steder registrert
      Når brukeren trykker på legg-til-nytt-sted-knappen
      Så skal forespørselen nektes
      Og brukeren skal få tilbakemelding om at han/hun allerede har maks antall steder registrert

    Scenario: Bruker har ikke registrert maks antall steder
      Gitt at bruker har mindre enn ti steder registrert
      Når brukeren trykker på legg-til-nytt-sted-knappen
      Så skal forespørselen aksepteres
      Og brukeren sendes til en ny stedsinnstillings-side