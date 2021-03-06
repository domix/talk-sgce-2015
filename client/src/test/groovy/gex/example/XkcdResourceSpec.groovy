package gex.example

import gex.example.client.v1.*
import org.springframework.beans.factory.annotation.Value 
import org.springframework.test.context.ContextConfiguration 
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.IntegrationTest

import spock.lang.*

@IntegrationTest
@WebAppConfiguration 
@ContextConfiguration(loader = SpringApplicationContextLoader, classes = Application)
class XkcdResourceSpec extends Specification {

  @Shared
  ApiClient client

  def setup() {
    client = Builder.create().buildApi()
  }

  def 'We can get simple xkcd data'() {
    when:
      def result = client.getXkcd().toBlocking().single()
    then:
      result
      result.img
  }

}

