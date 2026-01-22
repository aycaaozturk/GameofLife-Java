package jpp.gol.logic;

import jpp.gol.model.World;

/**
 *  Interface for classes that need to get informed about changes to the world.
 */
public interface WorldChangedListener {
//    WorldChangedListener, genellikle bir oyun döngüsünde her iterasyondan sonra World nesnesinde meydana gelen
//    değişiklikleri gözlemlemek ve bu değişiklikler hakkında belirli sınıfları bilgilendirmek için kullanılır.
//    Bu bilgilendirme, GUI gibi bir arayüzün dünya durumunu güncelleyebilmesi veya başka bir işlem gerçekleştirebilmesi için
//    gereklidir.
//
    /**
     * Called with the new world state.
     *
     * @param world the current world.
     */
    void onChange(World world);
}

