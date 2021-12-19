package com.example.cookgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var stateNow = StateDesert.SELECT
    var typeDesrt = TypeDesert.COOKIE
    var timesIngredientOne = 0
    var ingredientsOne = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null){
            timesIngredientOne = savedInstanceState.getInt("desert_times_ingredient_one")
            ingredientsOne = savedInstanceState.getInt("desert_ingredients_one")
        }
        imgCupcake.setOnClickListener(View.OnClickListener {
            firstChoice(TypeDesert.COOKIE)
            selectDesert() })
        imgFruit.setOnClickListener(View.OnClickListener {
            firstChoice(TypeDesert.FRUIT)
            selectDesert() })
        imgCupcake.setOnLongClickListener(View.OnLongClickListener { showSnackBar() })
        timesRandomIngredientOne()
    }
    fun firstChoice(type:TypeDesert){
        if(stateNow == StateDesert.SELECT){
        typeDesrt = type}
    }
    fun hideOrAppearImageFruit(stateImage:Boolean){
        when(stateImage){
        true -> imgFruit.visibility = View.VISIBLE
        else -> imgFruit.visibility = View.GONE}
    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putInt("desert_times_ingredient_one",timesIngredientOne)
        outState.putInt("desert_ingredients_one",ingredientsOne)
        super.onSaveInstanceState(outState, outPersistentState)
    }
    fun selectDesert(){
        if(stateNow == StateDesert.SELECT){
            stateNow = StateDesert.INGREDIENT_ONE
        }else if(stateNow == StateDesert.INGREDIENT_ONE){
            if(finishIngredient()){stateNow = StateDesert.INGREDIENT_TWO}
        }else if(stateNow == StateDesert.INGREDIENT_TWO){
            if(finishIngredient()){stateNow = StateDesert.INGREDIENT_THREE}
        }else if(stateNow == StateDesert.INGREDIENT_THREE){
            if(finishIngredient()){stateNow = StateDesert.EAT}
        }else if(stateNow == StateDesert.EAT){
            showSnackBar()
            stateNow = StateDesert.PLATE
        }else{
            stateNow = StateDesert.SELECT
            timesRandomIngredientOne()
        }
        setImageAndText()
    }
    fun finishIngredient():Boolean{
        ingredientsOne++
        showSnackBar()
        if(ingredientsOne == timesIngredientOne){
            timesRandomIngredientOne()
            return true}
        return false
    }
    fun setImageAndText(){
        if(stateNow == StateDesert.SELECT){
            imgCupcake.setImageResource(R.drawable.cookie)
            hideOrAppearImageFruit(true)
            txtCupcake.text = getString(R.string.select_desert)
        }else if(stateNow == StateDesert.INGREDIENT_ONE){
            hideOrAppearImageFruit(false)
                imgCupcake.setImageResource(if(typeDesrt == TypeDesert.COOKIE)R.drawable.chocolate else R.drawable.banana)
                txtCupcake.text = getString(R.string.put_ingredient_one)
        }else if(stateNow == StateDesert.INGREDIENT_TWO){
            imgCupcake.setImageResource(if(typeDesrt == TypeDesert.COOKIE)R.drawable.egg else R.drawable.grapes)
            txtCupcake.text = getString(R.string.put_ingredient_two)
        }else if(stateNow == StateDesert.INGREDIENT_THREE){
            imgCupcake.setImageResource(if(typeDesrt == TypeDesert.COOKIE)R.drawable.flour else R.drawable.orangepng)
            txtCupcake.text = getString(R.string.put_ingredient_third)
        }else if(stateNow == StateDesert.EAT){
            imgCupcake.setImageResource(if(typeDesrt == TypeDesert.COOKIE)R.drawable.cookies else R.drawable.fruit)
            txtCupcake.text = getString(R.string.desert_eat)
        }else{
            imgCupcake.setImageResource(R.drawable.plate)
            txtCupcake.text = getString(R.string.desert_plate)
        }
    }
    fun timesRandomIngredientOne(){
        timesIngredientOne = (2..6).random()
        ingredientsOne = 0
    }
    private fun showSnackBar():Boolean{
        if(stateNow == StateDesert.INGREDIENT_ONE || (stateNow == StateDesert.INGREDIENT_TWO) || (stateNow == StateDesert.INGREDIENT_THREE)) {
            var text:String = ""
            if(ingredientsOne == timesIngredientOne){
                text = "keep the receipe"
            }
            else
            {
                text = "you put ${ingredientsOne} : you need put ${timesIngredientOne}"
            }
            Snackbar.make(findViewById(R.id.layoutCook),text,Snackbar.LENGTH_SHORT).show()
                return true
            }
        else if(stateNow == StateDesert.EAT){
            Snackbar.make(findViewById(R.id.layoutCook),"YUMMY",Snackbar.LENGTH_SHORT).show()
        }
        return false
    }
}