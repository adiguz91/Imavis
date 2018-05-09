package com.drone.imavis.mvp.data.local.db;

import android.content.Context;

import com.drone.imavis.mvp.data.model.DaoMaster;
import com.drone.imavis.mvp.data.model.FlyPlanDao;
import com.drone.imavis.mvp.di.ApplicationContext;
import com.drone.imavis.mvp.di.DatabaseInfo;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
public class DatabaseOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    public DatabaseOpenHelper(@ApplicationContext Context context, @DatabaseInfo String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        List<Migration> migrations = getMigrations();

        // Only run migrations past the old version
        for (Migration migration : migrations) {
            if (oldVersion < migration.getVersion()) {
                migration.runMigration(db);
            }
        }
    }

    private List<Migration> getMigrations() {
        List<Migration> migrations = new ArrayList<>();
        migrations.add(new MigrationV2());
        migrations.add(new MigrationV3());
        migrations.add(new MigrationV4());
        migrations.add(new MigrationV5());
        migrations.add(new MigrationV6());
        migrations.add(new MigrationV7());

        // Sorting just to be safe, in case other people add migrations in the wrong order.
        Comparator<Migration> migrationComparator = new Comparator<Migration>() {
            @Override
            public int compare(Migration m1, Migration m2) {
                return m1.getVersion().compareTo(m2.getVersion());
            }
        };
        Collections.sort(migrations, migrationComparator);
        return migrations;
    }

    private interface Migration {
        Integer getVersion();

        void runMigration(Database db);
    }

    private static class MigrationV2 implements Migration {

        @Override
        public Integer getVersion() {
            return 2;
        }

        @Override
        public void runMigration(Database db) {
            // Add new column OnlineId to project table
            db.execSQL("ALTER TABLE " + FlyPlanDao.TABLENAME + " ADD COLUMN " + FlyPlanDao.Properties.NodesJson.columnName + " TEXT");
        }
    }

    private static class MigrationV3 implements Migration {

        @Override
        public Integer getVersion() {
            return 3;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table

        }
    }

    private static class MigrationV4 implements Migration {

        @Override
        public Integer getVersion() {
            return 4;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table

        }
    }

    private static class MigrationV5 implements Migration {

        @Override
        public Integer getVersion() {
            return 5;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            /*db.execSQL("DROP TABLE " + ProjectDao.TABLENAME);
            db.execSQL("DROP TABLE " + FlyPlanDao.TABLENAME);
            ProjectDao.createTable(db, false);
            FlyPlanDao.createTable(db, false);*/

        }
    }

    private static class MigrationV6 implements Migration {

        @Override
        public Integer getVersion() {
            return 6;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            //db.execSQL("DROP TABLE " + ProjectDao.TABLENAME);
            //db.execSQL("DROP TABLE " + FlyPlanDao.TABLENAME);
            //ProjectDao.createTable(db, false);
            //FlyPlanDao.createTable(db, false);

        }
    }

    private static class MigrationV7 implements Migration {

        @Override
        public Integer getVersion() {
            return 7;
        }

        @Override
        public void runMigration(Database db) {
            // Add new column OnlineId to project table
            db.execSQL("ALTER TABLE " + FlyPlanDao.TABLENAME + " ADD COLUMN " + FlyPlanDao.Properties.IsClosed.columnName + " INTEGER default 0");
        }
    }
}
