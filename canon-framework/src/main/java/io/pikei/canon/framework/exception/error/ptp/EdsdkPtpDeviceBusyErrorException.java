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
package io.pikei.canon.framework.exception.error.ptp;

import io.pikei.canon.framework.api.constant.EdsdkError;

/**
 * <p>Created on 2019/04/11.</p>
 *
 * @author Yoann CAPLAIN
 * @since 1.2.1
 */
public class EdsdkPtpDeviceBusyErrorException extends EdsdkPtpErrorException {

    private static final long serialVersionUID = 1L;

    public EdsdkPtpDeviceBusyErrorException() {
        super(EdsdkError.EDS_ERR_PTP_DEVICE_BUSY.description(), EdsdkError.EDS_ERR_PTP_DEVICE_BUSY);
    }

    public EdsdkPtpDeviceBusyErrorException(final String message) {
        super(message, EdsdkError.EDS_ERR_PTP_DEVICE_BUSY);
    }

}
