package de.ftscraft.ftsengine.feature.items.instruments;

import org.bukkit.Sound;

public class InstrumentManager {

    public static final Instrument[] instruments = {
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_HARP, "Harfe"),                     //0
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_CHIME, "Chimes"),                   //1
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_FLUTE, "Flöte"),                    //2
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_BELL, "Glockenspiel"),              //3
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_GUITAR, "Laute"),                   //4
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_COW_BELL, "Kuhglocke"),             //5
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, "Knochenxylophon"),      //6
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, "Eisen-Xylophon"),  //7
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_BASS, "Bassgitarre")                //8
    };

}
