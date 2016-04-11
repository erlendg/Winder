# language: no

  Egenskap: Slett varsel

    Bakgrunn: Som bruker
              Vil jeg slette varsel for et registrert sted
              Slik at jeg ikke får varsler for dette stedet lenger

    Scenario: Bruker sletter et registrert sted
      Gitt at brukeren har åpnet appen
      Og har registrerte steder i listen
      Og har trykket på stedet for detaljoversikt
      Når brukeren trykker på slett-knappen
      Så skal stedet slettes
      Og brukeren returneres til hovedoversikten
      Og stedet er slettet fra listen med registrerte steder