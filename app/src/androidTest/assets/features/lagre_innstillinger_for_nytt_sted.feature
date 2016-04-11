# language: no

Egenskap: Lagre innstillinger for nytt sted

Bakgrunn: Som en bruker
          Vil jeg lagre innstillinger for et nytt sted
          Slik at jeg mottar varsel for stedet basert på disse innstillingene

Scenario: Bruker har valgt gyldige innstillinger for nytt sted
  Gitt at bruker har valgt et nytt sted
  Og er inne på innstillingssiden
  Når brukeren har valgt ønskede innstillinger for stedet
  Og trykker på lagre-knappen
  Så skal innstillingene lagres
  Og brukeren blir sendt tilbake til hovedsiden

Scenario: Bruker har ikke valgt gyldige innstillinger for nytt sted
  Gitt at bruker har valgt et nytt sted
  Og er inne på innstillingssiden
  Når brukeren trykker på lagre-knappen
  Og ikke har valgt gyldige innstillinger
  Så skal forespørselen nektes
  Og brukeren får en feilmelding