package dev.shingi.utils;

import java.util.ArrayList;
import java.util.List;

import dev.shingi.models.LedgerAccount;

public class LedgerAccountUtils {
    public static List<LedgerAccount> convertGrootboekenToLedgerAccounts(List<Grootboek> grootboeken) {
        List<LedgerAccount> ledgerAccounts = new ArrayList<>();

        for (Grootboek grootboek : grootboeken) {
            ledgerAccounts.add(new LedgerAccount(grootboek.getOmschrijving(), grootboek.getNummer(), grootboek.getId(), grootboek.getUri()));
        }

        return ledgerAccounts;
    }
}
