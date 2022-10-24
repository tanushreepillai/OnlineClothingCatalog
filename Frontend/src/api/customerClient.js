import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class CustomerClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getAllClothingSpecificType', 'getClothingById'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets all the clothing for the clothing name.
     * @param clothing name.Eg: shirt,pant
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns all the clothing with that name
     */

   /* async getAllClothingByType(errorCallback) {
                try {
                    const response = await this.client.get(`/clothing`);
                    return response.data;
                } catch (error) {
                    this.handleError("getAllClothingByType", error, errorCallback)
                }
            }*/

    async getAllClothingSpecificType(itemName,errorCallback) {
                    try {
                        const response = await this.client.get(`/clothing`);
                       return response.data;
                    } catch (error) {
                        this.handleError("getAllClothingSpecificType", error, errorCallback)
                    }
                }

    async getClothingById(id, errorCallback) {
                      try {
                           const response = await this.client.get(`/clothing/${clothingId}`);
                          return response.data;
                      } catch (error) {
                           this.handleError("getClothingById", error, errorCallback)
                      }
                   }


    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}