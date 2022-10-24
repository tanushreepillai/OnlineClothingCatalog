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
export default class BusinessClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'addClothing', 'getAllClothing', 'updateClothing', 'deleteClothing', 'getAllClothingSpecificType', 'getClothingById'];
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

    async getAllClothing(errorCallback) {
                try {
                    const response = await this.client.get(`/clothing/all`);
                    return response.data;
                } catch (error) {
                    this.handleError("getClothing", error, errorCallback)
                }
            }

    async addClothing(clothingId, itemName, inStock, price, itemSize, errorCallback) {
        try {
            const response = await this.client.post(`/clothing`, {
                "clothingId": clothingId,
                "itemName": itemName,
                "inStock": inStock,
                "price": price,
                "itemSize": itemSize
            });
            return response.data;
        } catch (error) {
            this.handleError("addClothing", error, errorCallback);
        }
    }

        async updateClothing(clothingId, itemName, inStock, price, itemSize, errorCallback) {
            try {
                const response = await this.client.put(`/clothing`, {
                    "clothingId": clothingId,
                    "itemName": itemName,
                    "inStock": inStock,
                    "price": price,
                    "itemSize": itemSize
                });
                return response.data;
            } catch (error) {
                this.handleError("updateClothing", error, errorCallback);
            }
        }

    async deleteClothing(clothingId, errorCallback) {
            try {
                await this.client.delete(`/clothing/${clothingId}`, {
                    "clothingId": clothingId,
                });
            } catch (error) {
                this.handleError("deleteClothing", error, errorCallback);
            }
        }

    async getAllClothingSpecificType(itemName, errorCallback) {
             try {
                 const response = await this.client.get(`/clothing/itemName/${itemName}` , {
                    "itemName": itemName,
                 });
                 return response.data;
             } catch (error) {
               this.handleError("getAllClothingSpecificType", error, errorCallback)
               }
             }

    async getClothingById(clothingId, errorCallback) {
              try {
                  const response = await this.client.get(`/clothing/${clothingId}`, {
                  "clothingId": clothingId,
                  });
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
