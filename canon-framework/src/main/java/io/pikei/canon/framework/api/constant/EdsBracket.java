/*
 * MIT License
 *
 * Copyright (c) 2018-2020 Yoann CAPLAIN
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.pikei.canon.framework.api.constant;

import org.blackdread.camerabinding.jna.EdsdkLibrary;
import io.pikei.canon.framework.api.constant.ConstantUtil;
import io.pikei.canon.framework.api.constant.NativeEnum;
import io.pikei.canon.framework.util.LibraryFieldUtil;

/**
 * Indicates the current bracket type.<br>
 * If multiple brackets have been set on the camera, you can get the bracket type as a logical sum.<br>
 * This property cannot be used to get bracket compensation.
 * Compensation is collected separately because there are separate properties for each bracket type.
 * <br>
 * See API Reference - 5.2.30 kEdsPropID_Bracket
 * <p>Created on 2018/10/05.<p>
 *
 * @author Yoann CAPLAIN
 * @since 1.0.0
 */
public enum EdsBracket implements NativeEnum<Integer> {
    kEdsBracket_AEB("AE bracket"),
    kEdsBracket_ISOB("ISO bracket"),
    kEdsBracket_WBB("WB bracket"),
    kEdsBracket_FEB("FE bracket"),
    kEdsBracket_Unknown("Bracket off");

    private final int value;
    private final String description;

    EdsBracket(final String description) {
        value = LibraryFieldUtil.getNativeIntValue(EdsdkLibrary.EdsBracket.class, name());
        this.description = description;
    }

    @Override
    public final Integer value() {
        return value;
    }

    @Override
    public final String description() {
        return description;
    }

    /**
     * @param value value to search
     * @return enum having same value as passed
     * @throws IllegalArgumentException if value was not found
     */
    public static EdsBracket ofValue(final Integer value) {
        return ConstantUtil.ofValue(EdsBracket.class, value);
    }

}
