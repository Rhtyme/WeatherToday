package com.rhtyme.weathertoday.injection.component;

import dagger.Subcomponent;
import com.rhtyme.weathertoday.injection.PerFragment;
import com.rhtyme.weathertoday.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}
