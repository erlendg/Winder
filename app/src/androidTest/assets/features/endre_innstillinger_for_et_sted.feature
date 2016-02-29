# language: no

  Egenskap:Endre innstillinger for et sted

    Bakgrunn: Som en bruker vil jeg endre innstillingene for et allerede registrert sted slik at kriteriene for varsel for dette stedet endres

    Scenario: Bruker endrer varselinnstillinger for sted
      Gitt at brukeren har funnet stedet i listen over registrerte steder
      Og har trykket på stedet for detaljoversikt
      Når brukeren trykker på endre-knappen
      Så skal varsel innstillingene for stedet åpnes
      Og brukeren vil kunne endre de allerede definerte innstillingene til stedet