package gex.example

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.grails.datastore.gorm.boot.autoconfigure.HibernateGormAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.StandardPasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

import org.springframework.cloud.netflix.hystrix.EnableHystrix


@EnableAutoConfiguration
@Configuration
@ComponentScan
@Import(HibernateGormAutoConfiguration)

@EnableHystrix

class CoreConfigTest {

  @Bean
  PasswordEncoder encoder() {
    new BCryptPasswordEncoder() 
  }

  @Bean
  TokenStore tokenStore() {
    new InMemoryTokenStore()
  }

  @Bean
  Gson buildGson() {
    new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
      // use ISO 8601 formatted date
      .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
      .serializeNulls()
      .create()
  }

}
