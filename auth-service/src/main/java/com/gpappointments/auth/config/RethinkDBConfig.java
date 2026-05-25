package com.gpappointments.auth.config;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RethinkDBConfig {

    @Value("${rethinkdb.host}")
    private String host;

    @Value("${rethinkdb.port}")
    private int port;

    @Value("${rethinkdb.database}")
    private String database;

    private static final RethinkDB r = RethinkDB.r;

    @Bean
    public Connection rethinkDBConnection() {
        return r.connection()
                .hostname(host)
                .port(port)
                .connect();
    }

    @PostConstruct
    public void initDatabase() {
        Connection conn = rethinkDBConnection();

        // create database if it does not exist
        boolean dbExists = r.dbList()
                .contains(database)
                .run(conn, Boolean.class)
                .single();

        if (!dbExists) {
            r.dbCreate(database).run(conn);
        }

        // create credentials table if it does not exist
        boolean tableExists = r.db(database)
                .tableList()
                .contains("credentials")
                .run(conn, Boolean.class)
                .single();

        if (!tableExists) {
            r.db(database)
                    .tableCreate("credentials")
                    .run(conn);

            // index on email so lookups are fast
            r.db(database)
                    .table("credentials")
                    .indexCreate("email")
                    .run(conn);
        }
    }
}
