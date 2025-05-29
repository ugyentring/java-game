package my.games.earthguardian.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import my.games.earthguardian.managers.ResourceManager;

public class UIFactory {
    // Static constants for widget names
    public static final String BUTTON = "button";
    public static final String LABEL = "label";
    public static final String TEXT_FIELD = "texdtfield";
    public static final String TEXT_AREA = "textarea";
    public static final String CHECK_BOX = "checkbox";
    public static final String IMAGE_BUTTON = "imagebutton";
    public static final String LIST = "list";
    public static final String SELECT_BOX = "selectbox";
    public static final String SLIDER = "slider";
    public static final String PROGRESS_BAR = "progressbar";
    public static final String WINDOW = "window";
    public static final String TOUCHPAD = "touchpad";
    public static final String DIALOG = "dialog";
    public static final String TABLE = "table";
    public static final String SCROLL_PANE = "scrollpane";
    private final Label.LabelStyle labelStyle;
    private Skin skin; // Skin used for styling the UI widgets
    private Label defaultLabel; // Default label with custom style
    // Constructor that takes a Skin argument
    public UIFactory(Skin skin) {
        this.skin = skin;
// Create a label with default font and yellow color
        labelStyle = new Label.LabelStyle();
        labelStyle.font = ResourceManager.getInstance().getDefaultFont();
        labelStyle.fontColor = Color.YELLOW;
        defaultLabel = new Label("Default Label", labelStyle);
    }
/**
 * Factory method to create UI widgets.
 * @param type The type of widget to create.
 * @param x The x position of the widget.
 * @param y The y position of the widget.
 * @param width The width of the widget.
 * @param height The height of the widget.
 * @param text Optional text for the widget.
 * @return The created widget.
 */
public Actor createWidget(String type, float x, float y, float width, float height, String text) {
    Actor widget;
    switch (type.toLowerCase()) {
        case BUTTON:
            TextButton button = new TextButton(text != null ? text : "Button", skin);
            button.getLabel().setStyle(labelStyle);
            widget = button;
            break;
        case LABEL:
            widget = new Label(text != null ? text : "Label", skin);
            break;
        case TEXT_FIELD:
            widget = new TextField(text != null ? text : "", skin);
            break;
        case TEXT_AREA:
            widget = new TextArea(text != null ? text : "", skin);
            break;
        case CHECK_BOX:
            widget = new CheckBox(text != null ? text : "CheckBox", skin);
            break;
        case IMAGE_BUTTON:
            widget = new ImageButton(skin);
            break;
        case LIST:
            widget = new List<>(skin);
            break;
        case SELECT_BOX:
            widget = new SelectBox<>(skin);
            break;
        case SLIDER:
            widget = new Slider(0, 100, 1, false, skin);
            break;
        case PROGRESS_BAR:
            widget = new ProgressBar(0, 100, 1, false, skin);
            break;
        case WINDOW:
            widget = new Window(text != null ? text : "Window", skin);
            break;

        case TABLE:
            widget = new Table(skin);
            break;
        case SCROLL_PANE:
            widget = new ScrollPane(null, skin);
            break;
        case TOUCHPAD:
// Load custom images for touchpad background and knob
            Texture backgroundRegion = ResourceManager.getInstance().getAsset(
                "images/touchBackground.png",Texture.class);
            Texture knobRegion = ResourceManager.getInstance().getAsset(
                "images/touchKnob.png",Texture.class);
// Create TouchpadStyle
            Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
            touchpadStyle.background = new TextureRegionDrawable(backgroundRegion);
            touchpadStyle.knob = new TextureRegionDrawable(knobRegion);
// Create Touchpad with custom style
            widget = new Touchpad(10, touchpadStyle);
            break;
        case DIALOG:
            widget = new Dialog(text != null ? text : "Dialog", skin);
            break;
        default:
            throw new IllegalArgumentException("Unknown widget type: " + type);
    }
    widget.setBounds(x, y, width, height); // Set the position and size of the widget
    return widget;
}
    /**
     * Getter for the default label.
     * @return The default label with custom style.
     */
    public Label getDefaultLabel() {
        return defaultLabel;
    }
}
