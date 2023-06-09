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
package io.pikei.canon.framework.api.helper.logic.event;

import org.blackdread.camerabinding.jna.EdsdkLibrary.EdsCameraRef;
import io.pikei.canon.framework.api.constant.EdsPropertyEvent;
import io.pikei.canon.framework.api.constant.EdsPropertyID;

/**
 * <p>Created on 2018/11/04.</p>
 *
 * @author Yoann CAPLAIN
 * @since 1.0.0
 */
public interface CanonPropertyEvent extends CanonEvent {

    /**
     * @return camera from which event is
     */
    EdsCameraRef getCameraRef();

    /**
     * @return event type that occurred
     */
    EdsPropertyEvent getPropertyEvent();

    /**
     * @return the property ID created by the event
     */
    EdsPropertyID getPropertyId();

    /**
     * Used to identify information created by the event for custom function (CF) properties or other properties that have multiple items of information.
     *
     * @return inParam from camera
     */
    long getInParam();

}
