package com.homebudget.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ContractorEntity.class, PaymentEntity.class},
        views = {PaymentView.class},
        version = 35)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ContractorDAO contractorDAO();

    public abstract PaymentDAO paymentDAO();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "itjet").addMigrations(MIGRATION_34_35)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_34_35 = new Migration(34, 35) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("drop view v_payment");
            database.execSQL("create view `v_payment` AS Select contractor.contractor,contractor.service,contractor.account,payment.id,payment.amount, payment.term, payment.status from payment INNER JOIN contractor ON payment.contractorid = contractor.id");
        }
    };

}
