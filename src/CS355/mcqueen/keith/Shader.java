package CS355.mcqueen.keith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Shader {
    public Shader(LightSource... lights) {
        for (LightSource lightSource : lights) {
            this.addLightSource(lightSource);
        }
    }

    private final List<LightSource> lightSources = new ArrayList<>();

    public void addLightSource(LightSource lightSource) {
        this.lightSources.add(lightSource);
    }

    public abstract void shade(Triangle3D triangle, Color baseColor);

    protected List<LightSource> getLightSources() {
        return Collections.unmodifiableList(this.lightSources);
    }
}
