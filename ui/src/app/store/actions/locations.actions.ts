import { createAction, props } from '@ngrx/store';
import { Location } from 'src/app/core/models';

export const loadAllLocations = createAction('[LOCATIONS] load all locations');

export const loadAllLocationsByLoginTag = createAction('[LOCATIONS] load all locations');

export const loadLoginLocations = createAction(
  '[LOCATIONS] load login locations'
);

export const loadLocationById = createAction(
  '[LOCATIONS] load location By Location ID',
  props<{ locationUuid: string }>()
);

export const addLoadedLocations = createAction(
  '[LOCATIONS] add loaded locations',
  props<{ locations: Location[] }>()
);

export const loadingLocationsFails = createAction(
  '[LOCATIONS] loading locations fails',
  props<{ error: any }>()
);

export const setCurrentUserCurrentLocation = createAction(
  '[LOCATIONS] set current user current location',
  props<{ location: any }>()
);

export const upsertLocation = createAction(
  '[LOCATIONS] upsert location',
  props<{ location: Location }>()
);

export const loadLocationsByTagName = createAction(
  '[LOCATIONS] load locations by tag name',
  props<{ tagName: string }>()
);

export const loadingLocationByTagNameFails = createAction(
  '[LOCATIONS] loading locations by Tag name fails',
  props<{ error: any }>()
);

export const clearLocations = createAction('[LOCATIONS] clear locations');

export const updateCurrentLocationStatus = createAction(
  '[CURRENT LOCATION] update current location',
  props<{ settingLocation: boolean }>()
);