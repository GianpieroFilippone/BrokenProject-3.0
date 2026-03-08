package com.lipari.bank.pattern;

import java.math.BigDecimal;

/**
 * Configurazione globale della banca — pattern Singleton.
 */
public class BankConfiguration {


    /**
     * BUG: il Singleton non è thread-safe. Il campo 'instance' non è dichiarato volatile,
     * quindi il double-checked locking non garantisce la visibilità corretta tra thread.
     * A causa del reordering del Java Memory Model, alcuni thread possono vedere
     * 'instance' come non-null pur essendo ancora parzialmente inizializzata,
     * producendo più istanze diverse in test multithreading.
     * Soluzione: dichiarare 'private static volatile BankConfiguration instance'.
     */

    // Campo corretto per il Double‑Checked Locking
    private static volatile BankConfiguration instance;


    // ─── Proprietà di configurazione ────────────────────────────────────────

    private final String     bankName;
    private final String     bankCode;
    private final BigDecimal maxDailyWithdrawLimit;
    private final int        maxAccountsPerCustomer;
    private final int        sessionTimeoutSeconds;

    // ─── Costruttore privato ─────────────────────────────────────────────────

    private BankConfiguration() {
        // Simula caricamento da file/database — introduce latenza artificiale
        // per allargare la finestra di race condition nel test con molti thread.
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.bankName               = "LipariBank S.p.A.";
        this.bankCode               = "IT-LB-001";
        this.maxDailyWithdrawLimit  = new BigDecimal("10000.00");
        this.maxAccountsPerCustomer = 5;
        this.sessionTimeoutSeconds  = 300;
    }

    // ─── getInstance — Double-Checked Locking ───────────────────────────────

    public static BankConfiguration getInstance() {
        if (instance == null) {
            synchronized (BankConfiguration.class) {
                if (instance == null) {
                    instance = new BankConfiguration();
                }
            }
        }
        return instance;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public String     getBankName()               { return bankName; }
    public String     getBankCode()               { return bankCode; }
    public BigDecimal getMaxDailyWithdrawLimit()  { return maxDailyWithdrawLimit; }
    public int        getMaxAccountsPerCustomer() { return maxAccountsPerCustomer; }
    public int        getSessionTimeoutSeconds()  { return sessionTimeoutSeconds; }

    @Override
    public String toString() {
        return String.format("BankConfiguration{name='%s', code='%s', @%08x}",
                bankName, bankCode, System.identityHashCode(this));
    }
}
