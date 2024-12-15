package de.ftscraft.ftsengine.feature.instruments;

import org.bukkit.Sound;

public class InstrumentManager {

    public static Instrument[] instruments = {
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_HARP, "Harfe"),                   //0
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_CHIME, "Glockenspiel"),           //1
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_FLUTE, "Flöte"),                  //2
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_BELL, "Glocke"),                  //3
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_GUITAR, "Gitarre"),               //4
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_COW_BELL, "Kuhglocke"),           //5
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, "Xylophon"),           //6
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, "Eisen-Xylophon"),//7
            new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_BASS, "Bassgitarre")              //8
    };

}