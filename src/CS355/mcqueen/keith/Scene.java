package CS355.mcqueen.keith;

import java.util.*;
import java.util.function.Consumer;

public class Scene implements Iterable<Model3D> {
    private final List<Model3D> models = new ArrayList<>();
    private final List<LightSource> lights = new ArrayList<>();
    private final int width;
    private final int height;
    private Color backgroundColor = new Color(0.0d, 0.0d, 0.0d);

    public Scene(int width, int height, Color backgroundColor) {
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
    }

    public void addModel(Model3D model) {
        this.models.add(model);
//        System.out.println("model = " + model);
    }

    public void removeModel(Model3D model) {
        this.models.remove(model);
    }

    @Override
    public Iterator<Model3D> iterator() {
        return this.models.iterator();
    }

    @Override
    public void forEach(Consumer<? super Model3D> action) {
        this.models.forEach(action);
    }

    @Override
    public Spliterator<Model3D> spliterator() {
        return this.models.spliterator();
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void addLight(LightSource light) {
        this.lights.add(light);
    }

    public void removeLight(LightSource light) {
        this.lights.remove(light);
    }

    public List<LightSource> getLights() {
        return Collections.unmodifiableList(this.lights);
    }
}
