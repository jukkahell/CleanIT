import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import com.mchange.v2.c3p0.ComboPooledDataSource
import grails.util.Environment

// Place your Spring DSL code here
beans = {
    if (Environment.current != Environment.TEST) {
        dataSource(ComboPooledDataSource) { bean ->
            bean.destroyMethod = "close";
            user = CH.config.dataSource.username;
            password = CH.config.dataSource.password;
            driverClass = CH.config.dataSource.driverClassName;
            jdbcUrl = CH.config.dataSource.url;

            maxStatements = 180; // prepared statement pooling maxConnectionAge = 4 * 60 * 60 // 4 hours
            initialPoolSize = 5;
            maxPoolSize = 20;
            minPoolSize = 5;
            acquireIncrement = 3;
            idleConnectionTestPeriod = 3600;
            maxIdleTime = 10800;
        }
    }
}
