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
package io.pikei.canon.framework.exception.error.general;

import io.pikei.canon.framework.api.constant.EdsdkError;

/**
 * <p>Created on 2018/12/16.</p>
 *
 * @author Yoann CAPLAIN
 * @since 1.0.1
 */
public class EdsdkGeneralNotSupportedErrorException extends EdsdkGeneralErrorException {

    private static final long serialVersionUID = 1L;

    public EdsdkGeneralNotSupportedErrorException() {
        super(EdsdkError.EDS_ERR_NOT_SUPPORTED.description(), EdsdkError.EDS_ERR_NOT_SUPPORTED);
    }

    public EdsdkGeneralNotSupportedErrorException(final String message) {
        super(message, EdsdkError.EDS_ERR_NOT_SUPPORTED);
    }

}
