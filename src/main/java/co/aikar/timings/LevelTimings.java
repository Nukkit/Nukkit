/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
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
package co.aikar.timings;

import cn.nukkit.level.Level;

public class LevelTimings {
    public final Timing doChunkUnload;
    public final Timing doTickPending;
    public final Timing doChunkGC;
    public final Timing doTick;

    public final Timing tickChunks;
    public final Timing entityTick;
    public final Timing blockEntityTick;

    public final Timing syncChunkSendTimer;
    public final Timing syncChunkSendPrepareTimer;
    public final Timing syncChunkLoadTimer;
    public final Timing syncChunkLoadDataTimer;
    public final Timing syncChunkLoadEntitiesTimer;
    public final Timing syncChunkLoadBlockEntitiesTimer;

    public LevelTimings(Level level) {
        String name = level.getFolderName() + " - ";

        this.doChunkUnload = TimingsManager.getTiming(name + "doChunkUnload");
        this.doTickPending = TimingsManager.getTiming(name + "doTickPending");
        this.doChunkGC = TimingsManager.getTiming(name + "doChunkGC");
        this.doTick = TimingsManager.getTiming(name + "doTick");

        this.tickChunks = TimingsManager.getTiming(name + "tickChunks");
        this.entityTick = TimingsManager.getTiming(name + "entityTick");
        this.blockEntityTick = TimingsManager.getTiming(name + "blockEntityTick");

        this.syncChunkSendTimer = TimingsManager.getTiming(name + "syncChunkSend");
        this.syncChunkSendPrepareTimer = TimingsManager.getTiming(name + "syncChunkSendPrepare");
        this.syncChunkLoadTimer = TimingsManager.getTiming(name + "syncChunkLoad");
        this.syncChunkLoadDataTimer = TimingsManager.getTiming(name + "syncChunkLoad - Data");
        this.syncChunkLoadEntitiesTimer = TimingsManager.getTiming(name + "syncChunkLoad - Entities");
        this.syncChunkLoadBlockEntitiesTimer = TimingsManager.getTiming(name + "syncChunkLoad - BlockEntities");
    }
}
