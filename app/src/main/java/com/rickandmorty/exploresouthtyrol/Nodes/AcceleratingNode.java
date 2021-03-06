/*
 * Copyright 2018 Google LLC
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
package com.rickandmorty.exploresouthtyrol.Nodes;

import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.QuaternionEvaluator;
import com.google.ar.sceneform.math.Vector3;

/** Node that reacts to acceleration. */
public class AcceleratingNode extends Node {
    // We'll use Property Animation to make this node rotate.
    @Nullable
    private ObjectAnimator rotateAnimation = null;
    private final float degreesPerSecond;

    private final float G = 9.81f;
    private final float MAGIC = 1e-5f;

    private final float mass;
    private Vector3 speed;

    public AcceleratingNode(float mass, Vector3 initialForce, float degreesPerSecond) {
        this.mass = mass;
        this.speed = initialForce;
        this.degreesPerSecond = degreesPerSecond;
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        super.onUpdate(frameTime);

        if (this.getWorldPosition().y <= -3 && this.speed.y <= 0) {
            this.stopAnimation();
            this.setEnabled(false);
            return;
        }

        Vector3 gravityForce = new Vector3(0, -G * this.mass, 0).scaled(frameTime.getDeltaSeconds());
        this.speed = Vector3.add(this.speed, gravityForce);

        this.setWorldPosition(Vector3.add(this.getWorldPosition(), this.speed.scaled(MAGIC)));

        // Rotate
        float animatedFraction = rotateAnimation.getAnimatedFraction();
        rotateAnimation.setDuration(getAnimationDuration());
        rotateAnimation.setCurrentFraction(animatedFraction);
    }

    private long getAnimationDuration() {
        return (long) (1000 * 360 / degreesPerSecond);
    }

    @Override
    public void onActivate() {
        startAnimation();
    }

    @Override
    public void onDeactivate() {
        stopAnimation();
    }

    private void startAnimation() {
        if (rotateAnimation != null) {
            return;
        }

        rotateAnimation = createAnimator();
        rotateAnimation.setTarget(this);
        rotateAnimation.setDuration(getAnimationDuration());
        rotateAnimation.start();
    }

    private void stopAnimation() {
        if (rotateAnimation == null) {
            return;
        }

        rotateAnimation.cancel();
        rotateAnimation = null;
    }

    /** Returns an ObjectAnimator that makes this node rotate. */
    private static ObjectAnimator createAnimator() {
        float x = (float) Math.random();
        float y = (float) Math.random();
        float z = (float) Math.random();

        Vector3 axis = (new Vector3(x, y, z)).normalized();

        Quaternion orientation1 = Quaternion.axisAngle(axis, 0);
        Quaternion orientation2 = Quaternion.axisAngle(axis, 120);
        Quaternion orientation3 = Quaternion.axisAngle(axis, 240);
        Quaternion orientation4 = Quaternion.axisAngle(axis, 360);

        ObjectAnimator rotateAnimation = new ObjectAnimator();
        rotateAnimation.setObjectValues(orientation1, orientation2, orientation3, orientation4);

        // Next, give it the localRotation property.
        rotateAnimation.setPropertyName("localRotation");

        // Use Sceneform's QuaternionEvaluator.
        rotateAnimation.setEvaluator(new QuaternionEvaluator());

        //  Allow rotateAnimation to repeat forever
        rotateAnimation.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnimation.setRepeatMode(ObjectAnimator.RESTART);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setAutoCancel(true);

        return rotateAnimation;
    }
}
