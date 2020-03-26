/*
 * Copyright 2019 Web3 Labs Ltd.
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
package org.web3j.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.web3j.exceptions.MessageDecodingException;
import org.web3j.exceptions.MessageEncodingException;

/**
 * Message codec functions.
 *
 * <p>Implementation as per https://github.com/ethereum/wiki/wiki/JSON-RPC#hex-value-encoding
 */
public final class Numeric {

    private static final String HEX_PREFIX = "0x";
    private static final char[] HEX_CHAR_MAP = "0123456789abcdef".toCharArray();

    private Numeric() {}

    public static String encodeQuantity(final BigInteger value) {
        if (value.signum() != -1) {
            return HEX_PREFIX + value.toString(16);
        } else {
            throw new MessageEncodingException("Negative values are not supported");
        }
    }

    public static BigInteger decodeQuantity(final String value) {
        if (isLongValue(value)) {
            return BigInteger.valueOf(Long.parseLong(value));
        }

        if (!isValidHexQuantity(value)) {
            throw new MessageDecodingException("Value must be in format 0x[1-9]+[0-9]* or 0x0");
        }
        try {
            return new BigInteger(value.substring(2), 16);
        } catch (final NumberFormatException e) {
            throw new MessageDecodingException("Negative ", e);
        }
    }

    private static boolean isLongValue(final String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidHexQuantity(final String value) {
        if (value == null) {
            return false;
        }

        if (value.length() < 3) {
            return false;
        }

        if (!value.startsWith(HEX_PREFIX)) {
            return false;
        }

        // If TestRpc resolves the following issue, we can reinstate this code
        // https://github.com/ethereumjs/testrpc/issues/220
        // if (value.length() > 3 && value.charAt(2) == '0') {
        //    return false;
        // }

        return true;
    }

    public static String cleanHexPrefix(final String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }

    public static String prependHexPrefix(final String input) {
        if (!containsHexPrefix(input)) {
            return HEX_PREFIX + input;
        } else {
            return input;
        }
    }

    public static boolean containsHexPrefix(final String input) {
        return !Strings.isEmpty(input)
                && input.length() > 1
                && input.charAt(0) == '0'
                && input.charAt(1) == 'x';
    }

    public static BigInteger toBigInt(final byte[] value, final int offset, final int length) {
        return toBigInt((Arrays.copyOfRange(value, offset, offset + length)));
    }

    public static BigInteger toBigInt(final byte[] value) {
        return new BigInteger(1, value);
    }

    public static BigInteger toBigInt(final String hexValue) {
        final String cleanValue = cleanHexPrefix(hexValue);
        return toBigIntNoPrefix(cleanValue);
    }

    public static BigInteger toBigIntNoPrefix(final String hexValue) {
        return new BigInteger(hexValue, 16);
    }

    public static String toHexStringWithPrefix(final BigInteger value) {
        return HEX_PREFIX + value.toString(16);
    }

    public static String toHexStringNoPrefix(final BigInteger value) {
        return value.toString(16);
    }

    public static String toHexStringNoPrefix(final byte[] input) {
        return toHexString(input, 0, input.length, false);
    }

    public static String toHexStringWithPrefixZeroPadded(final BigInteger value, final int size) {
        return toHexStringZeroPadded(value, size, true);
    }

    public static String toHexStringWithPrefixSafe(final BigInteger value) {
        String result = toHexStringNoPrefix(value);
        if (result.length() < 2) {
            result = Strings.zeros(1) + result;
        }
        return HEX_PREFIX + result;
    }

    public static String toHexStringNoPrefixZeroPadded(final BigInteger value, final int size) {
        return toHexStringZeroPadded(value, size, false);
    }

    private static String toHexStringZeroPadded(
            final BigInteger value, final int size, final boolean withPrefix) {
        String result = toHexStringNoPrefix(value);

        final int length = result.length();
        if (length > size) {
            throw new UnsupportedOperationException(
                    "Value " + result + "is larger then length " + size);
        } else if (value.signum() < 0) {
            throw new UnsupportedOperationException("Value cannot be negative");
        }

        if (length < size) {
            result = Strings.zeros(size - length) + result;
        }

        if (withPrefix) {
            return HEX_PREFIX + result;
        } else {
            return result;
        }
    }

    public static byte[] toBytesPadded(final BigInteger value, final int length) {
        final byte[] result = new byte[length];
        final byte[] bytes = value.toByteArray();

        final int bytesLength;
        final int srcOffset;
        if (bytes[0] == 0) {
            bytesLength = bytes.length - 1;
            srcOffset = 1;
        } else {
            bytesLength = bytes.length;
            srcOffset = 0;
        }

        if (bytesLength > length) {
            throw new RuntimeException("Input is too large to put in byte array of size " + length);
        }

        final int destOffset = length - bytesLength;
        System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength);
        return result;
    }

    public static byte[] hexStringToByteArray(final String input) {
        final String cleanInput = cleanHexPrefix(input);

        final int len = cleanInput.length();

        if (len == 0) {
            return new byte[] {};
        }

        final byte[] data;
        final int startIdx;
        if (len % 2 != 0) {
            data = new byte[(len / 2) + 1];
            data[0] = (byte) Character.digit(cleanInput.charAt(0), 16);
            startIdx = 1;
        } else {
            data = new byte[len / 2];
            startIdx = 0;
        }

        for (int i = startIdx; i < len; i += 2) {
            data[(i + 1) / 2] =
                    (byte)
                            ((Character.digit(cleanInput.charAt(i), 16) << 4)
                                    + Character.digit(cleanInput.charAt(i + 1), 16));
        }
        return data;
    }

    public static String toHexString(byte[] input, int offset, int length, boolean withPrefix) {
        final String output = new String(toHexCharArray(input, offset, length, withPrefix));
        return withPrefix ? new StringBuilder(HEX_PREFIX).append(output).toString() : output;
    }

    private static char[] toHexCharArray(byte[] input, int offset, int length, boolean withPrefix) {
        final char[] output = new char[length << 1];
        for (int i = offset, j = 0; i < length; i++, j++) {
            final int v = input[i] & 0xFF;
            output[j++] = HEX_CHAR_MAP[v >>> 4];
            output[j] = HEX_CHAR_MAP[v & 0x0F];
        }
        return output;
    }

    public static String toHexString(final byte[] input) {
        return toHexString(input, 0, input.length, true);
    }

    public static byte asByte(final int m, final int n) {
        return (byte) ((m << 4) | n);
    }

    public static boolean isIntegerValue(final BigDecimal value) {
        return value.signum() == 0 || value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0;
    }
}
