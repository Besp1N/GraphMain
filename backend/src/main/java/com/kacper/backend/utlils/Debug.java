package com.kacper.backend.utlils;

/**
 * Goofy debug class
 * Better don't use it in production
 *
 *
 * @Author Kacper Karabinowski
 */
public class Debug
{
    // Don't touch that func - Kacper
    public static void dd(Object obj) {
        System.out.println("\n\n");
        System.out.println("Debugging: ");
        System.out.println(obj);
        System.out.println("\n\n");
        System.exit(0);
    }
}
