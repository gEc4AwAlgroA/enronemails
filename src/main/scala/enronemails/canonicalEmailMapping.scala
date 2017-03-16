package enronemails

object canonicalEmailMapping {
  // Less defined addresses must be lower amongst canonical group
  val canonicalMapping = Seq(
    "RAPP BILL &LT;BILL.RAPP@ENRON.COM&GT;" -> " CANONICAL_BILL_RAPP_AT_ENRON_COM ",
    "BATISTA DANIEL &LT;DANIEL.BATISTA@ENRON.COM&GT;" -> " CANONICAL_DANIEL_BATISTA_AT_ENRON_COM ",
    "DORNAN DARI &LT;DARI.DORNAN@ENRON.COM&GT;" -> " CANONICAL_DARI_DORNAN_AT_ENRON_COM ",
    "FOSSUM DREW &LT;DREW.FOSSUM@ENRON.COM&GT;" -> " CANONICAL_DREW_FOSSUM_AT_ENRON_COM ",
    "GADD ERIC &LT;ERIC.GADD@ENRON.COM&GT;" -> " CANONICAL_ERIC_GADD_AT_ENRON_COM ",
    "HASS GLEN &LT;GLEN.HASS@ENRON.COM&GT;" -> " CANONICAL_GLEN_HASS_AT_ENRON_COM ",
    "MOORE JAN &LT;JAN.MOORE@ENRON.COM&GT;" -> " CANONICAL_JAN_MOORE_AT_ENRON_COM ",
    "JOHN J LAVORATO &LT;JOHN J LAVORATO/ENRON@ENRONXGATE@ENRON&GT;" -> " CANONICAL_JOHN_LAVORATO_AT_ENRON_COM ",
    "WATSON KIMBERLY &LT;KIMBERLY.WATSON@ENRON.COM&GT;" -> " CANONICAL_KIMBERLY_WATSON_AT_ENRON_COM ",
    "ALPORT KYSA &LT;KYSA.ALPORT@ENRON.COM&GT;" -> " CANONICAL_KYSA_ALPORT_AT_ENRON_COM ",
    "DONOHO LINDY &LT;LINDY.DONOHO@ENRON.COM&GT;" -> " CANONICAL_LINDY_DONOHO_AT_ENRON_COM ",
    "LINDBERG LORRAINE &LT;LORRAINE.LINDBERG@ENRON.COM&GT;" -> " CANONICAL_LORRAINE_LINDBERG_AT_ENRON_COM ",
    "MCCONNELL MARK &LT;MARK_MCCONNELL@ENRON.NET&GT;" -> " CANONICAL_MARK_MCCONNELL_AT_ENRON_COM ",
    "KATZ MARTIN &LT;MARTIN.KATZ@ENRON.COM&GT;" -> " CANONICAL_MARTIN_KATZ_AT_ENRON_COM ",
    "DARVEAUX MARY &LT;MARY.DARVEAUX@ENRON.COM&GT;" -> " CANONICAL_MARY_DARVAUX_AT_ENRON_COM ",
    "MILLER MARY KAY &LT;MARY.KAY.MILLER@ENRON.COM&GT;" -> " CANONICAL_MARY_MILLER_AT_ENRON_COM ",
    "Y'BARBO PAUL &LT;PAUL.Y'BARBO@ENRON.COM&GT;" -> " CANONICAL_PAUL_YBARBO_AT_ENRON_COM ",

    "ALLEN PHILLIP K. &LT;PHILLIP.K.ALLEN@ENRON.COM&GT;" -> " CANONICAL_PHILLIP_ALLEN_AT_ENRON_COM ",
    "PALLEN@ENRON.COM &LT;PALLEN@ENRON.COM&GT;" -> " CANONICAL_PHILLIP_ALLEN_AT_ENRON_COM ",
    "PHILLIP ALLEN &LT;PALLEN@ENRON.COM&GT;" -> " CANONICAL_PHILLIP_ALLEN_AT_ENRON_COM ",
    "PHILLIP.K.ALLEN@ENRON.COM" -> " CANONICAL_PHILLIP_ALLEN_AT_ENRON_COM ",
    "PALLEN@ENRON.COM" -> " CANONICAL_PHILLIP_ALLEN_AT_ENRON_COM ",

    "LOPEZ RAQUEL &LT;RAQUEL.LOPEZ@ENRON.COM&GT;" -> " CANONICAL_RAQUEL_LOPEZ_AT_ENRON_COM ",
    "KILMER III ROBERT &LT;ROBERT.KILMER@ENRON.COM&GT;" -> " CANONICAL_ROBERT_KILMER_AT_ENRON_COM ",
    "CORMAN SHELLEY &LT;SHELLEY.CORMAN@ENRON.COM&GT;" -> " CANONICAL_SHELLEY_CORMAN_AT_ENRON_COM ",
    "DICKERSON STEVE V &LT;STEVE.V.DICKERSON@ENRON.COM&GT;" -> " CANONICAL_STEVE_DICKERSON_AT_ENRON_COM ",
    "KIRK STEVE &LT;STEVE.KIRK@ENRON.COM&GT;" -> " CANONICAL_STEVE_KIRK_AT_ENRON_COM ",
    "LOHMAN TK &LT;TK.LOHMAN@ENRON.COM&GT;" -> " CANONICAL_TK_LOHMAN_AT_ENRON_COM ",
    "ALONSO TOM &LT;TOM.ALONSO@ENRON.COM&GT;" -> " CANONICAL_TOM_ALONSO_AT_ENRON_COM "
  )

}