/*
 * Copyright 2019 Web3 Labs LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.protocol.pantheon;

import org.junit.Test;

import org.web3j.protocol.ResponseTester;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.pantheon.response.PantheonEthAccountsMapResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResponseTest extends ResponseTester {

    @Test
    public void testClicqueGetSigners() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": [\"0x42eb768f2244c8811c63729a21a3569731535f06\","
                        + "\"0x7ffc57839b00206d1ad20c69a1981b489f772031\","
                        + "\"0xb279182d99e65703f0076e4812653aab85fca0f0\"]\n"
                        + "}");

        EthAccounts ethAccounts = deserialiseResponse(EthAccounts.class);
        assertThat(
                ethAccounts.getAccounts().toString(),
                is(
                        "[0x42eb768f2244c8811c63729a21a3569731535f06, "
                                + "0x7ffc57839b00206d1ad20c69a1981b489f772031, "
                                + "0xb279182d99e65703f0076e4812653aab85fca0f0]"));
    }

    @Test
    public void testClicqueProposals() {
        buildResponse(
                "{\n"
                        + "    \"jsonrpc\": \"2.0\",\n"
                        + "    \"id\": 1,\n"
                        + "    \"result\": {\"0x42eb768f2244c8811c63729a21a3569731535f07\": false,"
                        + "\"0x12eb759f2222d7711c63729a45c3585731521d01\": true}\n}");

        PantheonEthAccountsMapResponse mapResponse =
                deserialiseResponse(PantheonEthAccountsMapResponse.class);
        assertThat(
                mapResponse.getAccounts().toString(),
                is(
                        "{0x42eb768f2244c8811c63729a21a3569731535f07=false, "
                                + "0x12eb759f2222d7711c63729a45c3585731521d01=true}"));
    }
}
