package CS355.mcqueen.keith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractFileModel extends TriangleModel {
    public AbstractFileModel(String file) {
        if (!this.isValidFile(file)) {
            throw new IllegalArgumentException("Invalid file: " + file);
        }

//        this.parseFile(file);
    }

    protected abstract boolean isValidFile(String file);

    protected void parseFile(String file) {
        try {
            Files.lines(Paths.get(file)).forEach(this::handleLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void handleLine(String line);
}
