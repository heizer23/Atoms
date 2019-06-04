package remmecke.mein.atoms;

import org.junit.Test;

import app.lerner2.projects.my.lerner4.FrageDatum;

import static org.junit.Assert.assertEquals;

public class FrageUnit {

    FrageDatum testFrage = new FrageDatum(1);


    @Test
    public void id_is_correct() {
        assertEquals(1, testFrage.getId());
    }
    @Test
    public void item_is_correct() {
        assertEquals("huh", testFrage.getItem());
    }

}
