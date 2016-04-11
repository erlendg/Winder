# language: no
Egenskap: Motta automatisk varsel

Bakgrunn: Som bruker
          Vil jeg motta automatisk varsel for et registrert sted
          Slik at jeg blir informert når ønsket vær inntreffer

Scenario: Bruker mottar automatisk varsel for sted
  Gitt at brukeren har registrert et sted for varsel
  Når de registrerte værinnstillingene inntreffer
  Så skal brukeren få et automatisk varsel fra appen