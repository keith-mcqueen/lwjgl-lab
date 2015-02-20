package CS355.mcqueen.keith;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Scene implements Iterable<Model3D> {
    private final List<Model3D> models = new ArrayList<>();
    private Color backgroundColor = new Color(0.0d, 0.0d, 0.0d);

    public Scene(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void addModel(Model3D model) {
        this.models.add(model);
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
}
