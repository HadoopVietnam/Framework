/*
 * Copyright 2012 Hadoop Vietnam <admin@hadoopvietnam.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hadoopvietnam.commons.crypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Hadoop Vietnam <admin@hadoopvietnam.com>
 */
public class J2MECrypto {

    private int[] S = new int[4];

    public J2MECrypto(byte[] key) {
        if (key == null) {
            throw new RuntimeException("Invalid key: Key was null");
        }
        if (key.length < 16) {
            throw new RuntimeException("Invalid key: Length was less than 16 bytes");
        }
        int off = 0;
        for (int i = 0; i < 4; i++) {
            this.S[i] = (key[(off++)] & 0xFF | (key[(off++)] & 0xFF) << 8 | (key[(off++)] & 0xFF) << 16 | (key[(off++)] & 0xFF) << 24);
        }
    }

    public byte[] encrypt(byte[] clear) {
        int paddedSize = (clear.length / 8 + (clear.length % 8 == 0 ? 0 : 1)) * 2;
        int[] buffer = new int[paddedSize + 1];
        buffer[0] = clear.length;
        pack(clear, buffer, 1);
        brew(buffer);
        return Base64.encodeBase64(unpack(buffer, 0, buffer.length * 4));
    }

    public byte[] decrypt(byte[] crypt) {
        crypt = Base64.decodeBase64(new String(crypt));
        int[] buffer = new int[crypt.length / 4];
        pack(crypt, buffer, 0);
        unbrew(buffer);
        return unpack(buffer, 1, buffer[0]);
    }

    void brew(int[] buf) {
        int i = 1;
        while (i < buf.length) {
            int n = 32;
            int v0 = buf[i];
            int v1 = buf[(i + 1)];
            int sum = 0;
            while (n-- > 0) {
                sum -= 1640531527;
                v0 += ((v1 << 4) + this.S[0] ^ v1) + (sum ^ v1 >>> 5) + this.S[1];
                v1 += ((v0 << 4) + this.S[2] ^ v0) + (sum ^ v0 >>> 5) + this.S[3];
            }
            buf[i] = v0;
            buf[(i + 1)] = v1;
            i += 2;
        }
    }

    void unbrew(int[] buf) {
        int i = 1;
        while (i < buf.length) {
            int n = 32;
            int v0 = buf[i];
            int v1 = buf[(i + 1)];
            int sum = -957401312;
            while (n-- > 0) {
                v1 -= ((v0 << 4) + this.S[2] ^ v0) + (sum ^ v0 >>> 5) + this.S[3];
                v0 -= ((v1 << 4) + this.S[0] ^ v1) + (sum ^ v1 >>> 5) + this.S[1];
                sum += 1640531527;
            }
            buf[i] = v0;
            buf[(i + 1)] = v1;
            i += 2;
        }
    }

    void pack(byte[] src, int[] dest, int destOffset) {
        int i = 0;
        int shift = 24;
        int j = destOffset;
        dest[j] = 0;
        while (i < src.length) {
            dest[j] |= (src[i] & 0xFF) << shift;
            if (shift == 0) {
                shift = 24;
                j++;
                if (j < dest.length) {
                    dest[j] = 0;
                }
            } else {
                shift -= 8;
            }
            i++;
        }
    }

    byte[] unpack(int[] src, int srcOffset, int destLength) {
        byte[] dest = new byte[destLength];
        int i = srcOffset;
        int count = 0;
        for (int j = 0; j < destLength; j++) {
            dest[j] = ((byte) (src[i] >> 24 - 8 * count & 0xFF));
            count++;
            if (count == 4) {
                count = 0;
                i++;
            }
        }
        return dest;
    }

    public static void main(String[] args) {
        J2MECrypto crypto = new J2MECrypto("LetfyActionCrypto".getBytes());
        String code = System.currentTimeMillis() + " " + "tk1cntt@gmail.com" + " " + System.currentTimeMillis();
        String encode = new String(Base64.encodeBase64(crypto.encrypt((code).getBytes())));
        System.out.println(encode);
        String decode = new String(crypto.decrypt(Base64.decodeBase64(encode.getBytes())));
        System.out.println(decode);
        System.out.println(DigestUtils.md5Hex(code));
    }
}
