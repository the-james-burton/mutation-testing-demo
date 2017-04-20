/**
 * The MIT License
 * Copyright (c) 2015 the-james-burton
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mutation.pojo;

import static java.lang.String.*;
import static java.util.stream.Collectors.*;
import static org.reflections.ReflectionUtils.*;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import javaslang.Tuple;
import javaslang.collection.CharSeq;
import javaslang.control.Try;

public class TickerLowQualityTest {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ObjectMapper json = new ObjectMapper();

  @Test
  public void testFullCoverageNoAssertions() throws Exception {
    // cover the constructors...
    String timestamp = OffsetDateTime.now().toString();
    Ticker ticker = Ticker.of(CharSeq.of("ABC.L"), ExchangeEnum.LSE, CharSeq.of("test name"));
    Ticker ticker2 = new Ticker(CharSeq.of("ABC.L"), CharSeq.of("test name"));
    Ticker ticker3 = new Ticker(CharSeq.of("ABC.L"), ExchangeEnum.LSE, CharSeq.of("test name"));
    Ticker ticker4 = new Ticker(timestamp, "ABC.L", "ABC", "LSE", "test name");

    // cover the getters...
    @SuppressWarnings("unchecked")
    Set<Method> getters = getAllMethods(Ticker.class,
        withModifier(Modifier.PUBLIC), withPrefix("get"), withParametersCount(0));

    List<Object> results = getters.stream()
        .map(m -> Tuple.of(m.getName(), Try.of(() -> m.invoke(ticker)).get()))
        .collect(toList());

    logger.info(format("properties:%s", results));

    // cover the object properties...
    logger.info(ticker.toString());
    ticker.equals(null);
    ticker.equals(ticker);
    ticker.hashCode();
    ticker.compareTo(ticker2);

  }

}
